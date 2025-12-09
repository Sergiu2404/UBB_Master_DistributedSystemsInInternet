function X = bern_sim(p, samples)

U = rand(1, samples);
X = (U <= p);

endfunction
