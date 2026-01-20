pkg load statistics
% SEM 2
% 1) Function rnd in Statistics Toolbox; special functions rand and randn.
% 10 samples from binomial(n = 20, p = 0.3)
x_b = binornd(20, 0.3, 1, 10) % 10 rand nums, each num represents total count of successes observed when repeating 20-trial experiment

% 5 samples from Poisson(lambda = 4)
x_p = poissrnd(4, 1, 5) % avg rate of events in a fixed period of time is 4 (ex: 4 events / day), experiment run 5 times

% 8 samples from Exponential(mean = 2)
x_e = exprnd(2, 1, 8) % 2 is avg waiting time until event occurs, waiting time experiment run 8 times

% 5 samples from Normal(mu = 10, sigma = 3)
x_n = normrnd(10, 3, 1, 5) % floats centered aorund mean 10, sigma = std dev (variability around mean), simulated 5 times

% BASIC random functions:
% Uniform(0, 1): all nums have the same chance to be chosen
rand() % one num
rand(1,5) % 5 floats between 0 and 1
rand(3,3) % 3x3 matrix of floats between 0 and 1
% Standard Normal N(0, 1): nr near the mean more likely to be chosen
randn() % one sample
randn(1,5) % row of 5 samples
randn(3,3) % 3x3 matrix

% Uniform(a, b)
a = 2; b = 5;
u = a + (b-a)*rand(1,10) % floats between 2 and 5

% Normal(miu, sigma)
mu = 10; sigma = 2;
x = mu + sigma * randn(1,10) % floats centered around 10

% Exponential (lambda)
lambda = 3;
x = -log(rand(1,10)) / lambda % non negative floats, highly skewed towards 0


% 2) Using discrete methods and a Standard Uniform U(0, 1) random number generator,
% write Matlab codes that simulate the following common discrete probability distributions:
% a) Bernoulli Distrib: simulate 10 trials (0 or 1)
p = 0.3;
samples = 10;
U = rand(1, samples);
X_Bern = (U <= p);
disp('Bernoulli(0.3) 10 samples: ');
disp(X_Bern);

% b) Binomial Distrib: total num of succ in 10 independent Bernoulli trials
n = 20;
p = 0.3;
samples = 10;
B_trials = (rand(n, samples) <= p);
X_Binom = sum(B_trials, 1);
disp('Binomial(20, 0.3) 10 samples (Integers 0-20)');
disp(X_Binom);

% c) Geometric Distrib: nr of failures (k) before first success
p = 0.5;
samples = 10;
U = rand(1, samples);
X_Geom = floor(log(U) ./ log(1 - p));
disp('c) Geometric(0.5) 1x10 Samples (Failures, Integers >= 0):');
disp(X_Geom);

% d) Negative Binomial Distribution NB(n, p)
n = 5; % Number of succ required
p = 0.4;
samples = 10;
U_matrix = rand(n, samples);
G_samples = floor(log(U_matrix) ./ log(1 - p));
X_NegBinom = sum(G_samples, 1);
disp('d) Negative Binomial(5, 0.4) 1x10 Samples (Failures, Integers >= 0):');
disp(X_NegBinom);

% e) Poisson Distribution P(lambda)
lambda = 4;
samples = 5;
X_Poisson = zeros(1, samples);

disp('e) Poisson(4) 1x5 Samples (Events Count, Integers >= 0):');


% using the defined functions:
p_bern = 0.3;
n_binom = 20;
p_binom = 0.3;
p_geo = 0.5;
n_nbinom = 5;
p_nbinom = 0.4;
lambda_poiss = 4;
num_samples = 10; % Except for Poisson, which uses 5

X_Bern = bern_sim(p_bern, num_samples);
disp('Bernoulli Samples: ');
disp(X_Bern);

X_Binom = binom_sim(n_binom, p_binom, num_samples);
disp('Binomial Samples: ');
disp(X_Binom);

X_Geom = geo_sim(p_geo, num_samples);
disp('Geometric Samples: ');
disp(X_Geom);

X_NegBinom = nbinom_sim(n_nbinom, p_nbinom, num_samples);
disp('Negative Binomial Samples: ');
disp(X_NegBinom);

X_Poisson = poisson_sim(lambda_poiss, 5); % 5 samples for Poisson
disp('Poisson Samples: ');
disp(X_Poisson);




% generate each of the 5 Poisson samples
% Parameters
lambda = 4; % avg rate of events / time unit
num_samples = 5;
X_Poisson = zeros(1, num_samples);
disp('e) Poisson(4) 1x5 Samples (Event Count in Unit Time):');
% for each of the 5 indep Poisson samples
for i = 1:num_samples
    count = 0;
    current_time = 0;

    % sum exponential times until the total time exceeds 1.0 (the fixed interval)
    while true % Loop continues until the unit time is exceeded
        % generate the next inter-arrival time T_i using Exp(lambda) formula
        T_i = -log(rand(1)) / lambda;
        % check if the next event occurs before the end of the fixed interval (t=1)
        if (current_time + T_i) < 1.0
            current_time = current_time + T_i; % advance time forward
            count = count + 1; % count event
        else
            break; % next event falls outside the interval (t=1), so stop counting for this sample
        endif
    endwhile

    X_Poisson(i) = count;
endfor
disp(X_Poisson);



% 3)
p = input('p in (0, 1): ');
err = input('error: ');
alpha = input('alpha (level of significance): ');

N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('nr of simulations: %d', N);

% a) Use the DITM to generate a Geo(p), p ∈ (0, 1), variable
X = ceil(log(1 - rand(1, N)) ./ log(1 - p) - 1) % Geo variables

fprintf('simulated probab. P(X = 2) = %1.5f\n', mean(X == 2));
fprintf('true probab. P(X = 2) = %1.5f\n', geopdf(2, p));
fprintf('error = %e\n\n', abs(geopdf(2, p) - mean(X == 2)));

fprintf('simulated probab. P(X <= 2) = %1.5f\n', mean(X <= 2));
fprintf('true probab. P(X <= 2) = %1.5f\n', geocdf(2, p));
fprintf('error = %e\n\n', abs(geocdf(2, p) - mean(X <= 2)));

fprintf('simulated probab. P(X < 2) = %1.5f\n', mean(X < 2));
fprintf('true probab. P(X < 2) = %1.5f\n', geocdf(1, p));
fprintf('error = %e\n\n', abs(geocdf(1, p) - mean(X < 2)));

fprintf('simulated mean E(X) = %5.5f\n', mean(X));
fprintf('true mean E(X) = %5.5f\n', (1 - p) / p);
fprintf('error = %e\n\n', abs((1 - p) / p - mean(X)));



% b) Then use that to generate a NB(n, p), n ∈ IN, p ∈ (0, 1), variable
p = input('p (in (0, 1)) = ');
n = input('n (positive integer) = ');

X = zeros(1, N);

% simulations
for i = 1:N
    % Generate n independent Geometric(p) variables (Y)
    % Each Y is the number of failures before the first success
    % Inverse Transform Method: X = floor(log(U) / log(1-p)).
    % The expression below is an algebraically equivalent form:
    Y = ceil(log(1 - rand(n, 1))/log(1 - p) - 1); % Geo variables

    % X(i) is the sum of the n Geometric variables (i.e., the Negative Binomial variable)
    X(i) = sum(Y);
end

% --- RESULTS OUTPUT (Matching the combined image logic) ---

% P(X = 2)
fprintf('simulated probab. P(X = 2) = %1.5f\n', mean(X == 2));
fprintf('true probab. P(X = 2) = %1.5f\n', nbinpdf(2, n, p));
fprintf('error = %e\n\n', abs(nbinpdf(2, n, p) - mean(X == 2)));

% P(X <= 2)
fprintf('simulated probab. P(X <= 2) = %1.5f\n', mean(X <= 2));
fprintf('true probab. P(X <= 2) = %1.5f\n', nbincdf(2, n, p));
fprintf('error = %e\n\n', abs(nbincdf(2, n, p) - mean(X <= 2)));

% P(X < 2) which is P(X <= 1)
fprintf('simulated probab. P(X < 2) = %1.5f\n', mean(X < 2));
fprintf('true probab. P(X < 2) = %1.5f\n', nbincdf(1, n, p));
fprintf('error = %e\n\n', abs(nbincdf(1, n, p) - mean(X < 2)));

% E(X)
fprintf('simulated mean E(X) = %5.5f\n', mean(X));
% Analytical Mean of Negative Binomial (failures before n successes): E(X) = n*(1-p)/p
fprintf('true mean E(X) = %5.5f\n', n * (1 - p) / p);
fprintf('error = %e\n\n', abs(n * (1 - p) / p - mean(X)));
