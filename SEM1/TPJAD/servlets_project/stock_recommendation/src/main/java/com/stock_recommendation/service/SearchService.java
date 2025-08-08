package com.stock_recommendation.service;

import com.stock_recommendation.entity.StockInfo;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



public class SearchService {
    private final Map<String, QuoteEntry> cache = new ConcurrentHashMap<>();
    private final long ttlMs;
    private boolean debugMode = false;

    private static class QuoteEntry {
        final StockInfo info;
        final long ts;
        QuoteEntry(StockInfo info) { this.info = info; this.ts = System.currentTimeMillis(); }
    }

    public SearchService(ServletContext ctx) {
        this(ctx, 60_000L); // 1 minute cache TTL
    }

    public SearchService(ServletContext ctx, long ttlMs) {
        this.ttlMs = ttlMs;
    }

    public int getCacheSize(){ return this.cache.size(); }
    public List<StockInfo> search(String query) {
        if (query == null || query.trim().isEmpty())
            return Collections.emptyList();
        String sym = query.trim().toUpperCase(Locale.ROOT);

        StockInfo info = getFromStooq(sym);
        if (info == null)
            return Collections.emptyList();
        return Collections.singletonList(info);
    }

    private StockInfo getFromStooq(String symbol) {
        long now = System.currentTimeMillis();
        QuoteEntry ce = cache.get(symbol);
        if (ce != null && (now - ce.ts) < ttlMs) return ce.info;

        String lc = symbol.toLowerCase(Locale.ROOT);
        List<String> candidates = Arrays.asList(lc + ".us", lc);

        for (String cand : candidates) {
            try {
                StockInfo info = fetchDailyCsvAndBuild(symbol, cand);
                if (info != null) {
                    cache.put(symbol, new QuoteEntry(info));
                    return info;
                }
            } catch (Exception e) {
                if (debugMode) e.printStackTrace();
            }
        }
        return null;
    }

    private StockInfo fetchDailyCsvAndBuild(String userSymbol, String stooqSymbol) throws Exception {
        String url = "https://stooq.com/q/d/l/?s=" + stooqSymbol + "&i=d";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(10000);
        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200) return null;

        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; } // skip header
                if (line.trim().isEmpty()) continue;
                String[] cols = line.split(",");

                if (cols.length >= 6) rows.add(cols);
            }
        }
        if (rows.isEmpty()) return null;

        String[] last = rows.get(rows.size() - 1);
        String[] prev = rows.size() >= 2 ? rows.get(rows.size() - 2) : null;

        BigDecimal close = parseDecimal(last[4]);
        Long volume = parseLong(last[5]);
        BigDecimal prevClose = prev != null ? parseDecimal(prev[4]) : null;

        BigDecimal change = null, changePct = null;
        if (close != null && prevClose != null) {
            change = close.subtract(prevClose);
            if (prevClose.signum() != 0) {
                changePct = change.multiply(BigDecimal.valueOf(100)).divide(prevClose, 4, BigDecimal.ROUND_HALF_UP);
            }
        }

        Date asOf = parseDate(last[0]);

        StockInfo info = new StockInfo();
        info.setSymbol(userSymbol);
        info.setName(userSymbol);           // no company name available
        info.setPrice(close);
        info.setPreviousClose(prevClose);
        info.setChange(change);
        info.setChangePercent(changePct);
        info.setVolume(volume);
        info.setAsOf(asOf);
        return info;
    }

    private static BigDecimal parseDecimal(String s) {
        try {
            if (s == null || s.isEmpty() || "null".equalsIgnoreCase(s)) return null;
            return new BigDecimal(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static Long parseLong(String s) {
        try {
            if (s == null || s.isEmpty() || "null".equalsIgnoreCase(s)) return null;
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static Date parseDate(String yyyyMmDd) throws ParseException {
        LocalDate ld = LocalDate.parse(yyyyMmDd);
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
