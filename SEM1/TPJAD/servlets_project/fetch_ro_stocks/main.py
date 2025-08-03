import yfinance as yf
import csv
import string

def normalize_industry(industry):
    i = (industry or "").lower()
    if any(k in i for k in ["energy", "oil", "gas", "petroleum"]):
        return "energy"
    elif any(k in i for k in ["health", "medical", "clinic"]):
        return "healthcare"
    elif any(k in i for k in ["bank", "finance", "insurance"]):
        return "financials"
    elif any(k in i for k in ["utilit", "electric", "power"]):
        return "utilities"
    elif any(k in i for k in ["telecom", "communication"]):
        return "telecom"
    elif any(k in i for k in ["real estate", "property"]):
        return "realestate"
    elif i.strip() == "":
        return "unknown"
    else:
        return "other"

valid_stocks = []

from itertools import product

for length in range(1, 5):
    for letters in product(string.ascii_uppercase, repeat=length):
        base_symbol = ''.join(letters)
        full_symbol = base_symbol + ".BX"

        try:
            ticker = yf.Ticker(full_symbol)
            info = ticker.info

            name = info.get("longName", "")
            industry = info.get("industry", "")

            if name and "N/A" not in name and info.get("quoteType") == "EQUITY":
                print(f"Found: {full_symbol} => {name}")
                valid_stocks.append({
                    "symbol": base_symbol,
                    "name": name,
                    "industry": normalize_industry(industry)
                })
        except Exception:
            continue

with open("bvb_discovered.csv", "w", newline='', encoding="utf-8") as f:
    writer = csv.DictWriter(f, fieldnames=["symbol", "name", "industry"])
    writer.writeheader()
    writer.writerows(valid_stocks)

print(f"Done. Found {len(valid_stocks)} valid Romanian stocks.")
