function X = nbinom_sim(n, p, samples)

U_matrix = rand(n, samples);
G_samples = floor(log(U_matrix) ./ log(1 - p));
X = sum(G_samples, 1);

endfunction
