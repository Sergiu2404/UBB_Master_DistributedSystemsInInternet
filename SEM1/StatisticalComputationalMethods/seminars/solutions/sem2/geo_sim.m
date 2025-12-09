function X = geo_sim(p, samples)

U = rand(1, samples);
X = floor(log(U) ./ log(1 - p));

endfunction
