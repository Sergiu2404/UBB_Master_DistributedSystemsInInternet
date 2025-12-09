function X = binom_sim(n, p, samples)

B_trials = (rand(n, samples) <= p);
X = sum(B_trials, 1);

endfunction
