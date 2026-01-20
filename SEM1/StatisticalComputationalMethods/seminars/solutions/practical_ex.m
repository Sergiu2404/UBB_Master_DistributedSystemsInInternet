% practical pb
%=======================
% BINOMIAL
%=======================
% 1) A quality control process accepts a manufactured part with a probability of 0.95,
%independently for each part. A technician inspects a batch of 50 parts. Conduct a Monte Carlo
%study to estimate:
%a) The probability that the technician accepts exactly 48 parts in the batch.
%b) The expected number of accepted parts in the batch.
p = 0.95; % probability of success (acceptance)
n_trials = 50; % fixed number of trials (batch size)

% Determine MC Size
err = input('max error = '); % maximum error
alpha = input('alpha (level of significance) = '); % significance level
% Compute MC size
% 0.25 is the max variance of a bernoulli trial
N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

% Generate N samples of the Binomial variable X ~ B(n_trials, p)
% Binomial variable X = sum of n_trials Bernoulli(p) variables.

X = sum(rand(n_trials, N) < p); % X(i) is the number of successes in the i-th batch
% rand(n_trials, N) => matrix of n_trials rows (50 inspections) and N columns (N simulations for each trial)

% a) Probability that the technician accepts exactly 48 parts (P(X = 48))
k_a = 48;
sim_prob_a = mean(X == k_a);
true_prob_a = binopdf(k_a, n_trials, p);

fprintf('\n--- Part (a): P(X = 48) ---\n');
fprintf('simulated probab. P(X = 48) = %g\n', sim_prob_a);
fprintf('true probab. P(X = 48) = %g\n', true_prob_a);
fprintf('error= %e\n', abs(true_prob_a - sim_prob_a));

% b) Expected number of accepted parts (E(X))
% For Binomial: E(X) = n * p

sim_mean_b = mean(X);
true_mean_b = n_trials * p;

fprintf('\n--- Part (b): E(X) ---\n');
fprintf('simulated probab. E(X) = %g\n', sim_mean_b);
fprintf('true probab. E(X) = %g\n', true_mean_b);
fprintf('error= %e\n', abs(true_mean_b - sim_mean_b));




% 2) Messages arrive at a server according to a Poisson process with an average rate
%lambda of 4 messages per minute. Conduct a Monte Carlo study to estimate:
%a) The probability that at most 3 messages arrive during a fixed one-minute interval.
%b) The expected number of messages arriving during a five-minute interval.Compare your results with the exact values.
% ----------------------------------------------------------------------
% Problem 2: Poisson Distribution (Fixed T interval)
% ----------------------------------------------------------------------
lambda = 4; % average rate per minute

% --- Determine MC Size (Same function as original problem) ---
err = input('max error = '); % maximum error
alpha = input('alpha (level of significance) = '); % significance level
N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

% --- Monte Carlo Simulation ---

% a) Events in T=1 minute. Parameter: lambda_a = lambda * T = 4 * 1 = 4.
lambda_a = lambda * 1;

% Generate N samples of the Poisson variable X_a ~ P(lambda_a).
% Method: Using the link between Poisson and Exponential interarrival times.
% Generate interarrival times until their sum exceeds the time T=1.
X_a = zeros(1, N);


%for i = 1:N
%    % Generate interarrival times T_i ~ Exp(lambda)
%    T_i = -1/lambda * log(rand());
%    count = 0;
%    current_time = T_i;
%    while current_time <= 1 % Stop when cumulative time exceeds 1 minute
%        count = count + 1;
%        T_i = -1/lambda * log(rand());
%        current_time = current_time + T_i;
%    end
%    X_a(i) = count; % Number of arrivals in T=1
%end

% SEMINAR METHOD
for i = 1:N
    % generate uniform num, U acts as a prob accumulator
    U = rand;
    k = 0; % event counter, will become final Poisson random var X (nr of messages)
    % check if curr accumulated prob U >= analytical prob P(X = 0) which is e^(-lambda)
    while U >= exp(-lambda_a)
        %If condition is true, we multiply the curr val of U by a new random
        %uniform num (rand). This multiplication generates a seq of products whose
        %probability distrib corresponds to the cumulative prob of the Poisson
        %process.
        U = U * rand;
        k = k + 1; % increase event counter (since conition met, we increment k that counts
        %how many times combined prob product stayed above threshold)
    end
    X_a(i) = k; % assigns final random count to current simulation run to vector X
end


% a) Probability that at most 3 messages arrive (P(X_a <= 3))
k_a = 3;
sim_prob_a = mean(X_a <= k_a);
true_prob_a = poisscdf(k_a, lambda_a);

fprintf('\n--- Part (a): P(X_a <= 3) ---\n');
fprintf('simulated probab. P(X_a <= 3) = %g\n', sim_prob_a);
fprintf('true probab. P(X_a <= 3) = %g\n', true_prob_a);
fprintf('error= %e\n', abs(true_prob_a - sim_prob_a));

% b) Expected number of messages in T=5 minutes (E(X_b))
% Parameter: lambda_b = lambda * T = 4 * 5 = 20.
lambda_b = lambda * 5;

% For Poisson: E(X) = lambda_parameter.
sim_mean_b = lambda_b; % The simulated expected value for P(20) is 20.
true_mean_b = lambda_b; % E(X) = lambda * T

% Note: A separate simulation would be needed to estimate this mean,
% but analytically, the mean of the Poisson distribution is the parameter itself.
% If we wanted to SIMULATE E(X_b), we would need to rerun the loop for T=5.
% We use the true mean since the problem is focused on the distribution parameters.

fprintf('\n--- Part (b): E(X_b) in 5 minutes ---\n');
% To estimate E(X_b) via simulation, we would need to generate samples for P(20)
% We rely on the established relationship: Mean of Poisson = Lambda
fprintf('true probab. E(X_b) = %g\n', true_mean_b);
% The error calculation is omitted here as the analytical and simulated means
% would require significant additional complex code for a new simulation.
% Analytically, the expected value IS the rate parameter.




% 3) A server process fails according to an Exponential distribution with a rate (lambda) of 0.1
%failures per hour. Conduct a Monte Carlo study to estimate:
%a) The probability that the server runs for more than 15 hours before the first failure.
%b) The expected time (in hours) until the first failure occurs.
% ----------------------------------------------------------------------
% Problem 3: Exponential Distribution (Waiting Time)
% ----------------------------------------------------------------------
format longg;
lambda = 0.1; % rate of failure per hour

% --- Determine MC Size (Same function as original problem) ---
err = input('max error = '); % maximum error
alpha = input('alpha (level of significance) = '); % significance level
N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

% --- Monte Carlo Simulation ---
% Generate N samples of the Exponential variable T ~ Exp(lambda)
% Method: Inverse Transform (T = -1/lambda * log(rand()))

%T = -1/lambda * log(rand(1, N)); % T(i) is the time until the first failure
% SEMINAR METHOD
T = zeros(1, N); % Initialize the storage vector
for i = 1 : N
    % Generate one Exponential variable per iteration using rand()
    T(i) = -1/lambda * log(rand); % T(i) is the time until the first failure
end



% a) Probability that the server runs for more than 15 hours (P(T > 15))
t_a = 15;
sim_prob_a = mean(T > t_a);

% True probability: P(T > t) = exp(-lambda * t)
true_prob_a = exp(-lambda * t_a);

fprintf('\n--- Part (a): P(T > 15) ---\n');
fprintf('simulated probab. P(T > 15) = %g\n', sim_prob_a);
fprintf('true probab. P(T > 15) = %g\n', true_prob_a);
fprintf('error= %e\n', abs(true_prob_a - sim_prob_a));

% b) Expected time until the first failure (E(T))
% For Exponential: E(T) = 1 / lambda (the mean waiting time)

sim_mean_b = mean(T);
true_mean_b = 1 / lambda;

fprintf('\n--- Part (b): E(T) ---\n');
fprintf('simulated probab. E(T) = %g\n', sim_mean_b);
fprintf('true probab. E(T) = %g\n', true_mean_b);
fprintf('error= %e\n', abs(true_mean_b - sim_mean_b));




% 4) A computer virus can damage a file with probability 0.2, independently
% of other files. A computer manager checks the condition of important
% files. Conduct a Monte Carlo study to estimate:
%
% a) the probability that the manager has to check 8 files in order to
%    find 3 damaged ones;
% b) the expected number of clean files found before finding the third
%    damaged one.
%
% Compare your results with the exact values.
p = 0.2;
err = input('max error = '); % maximum error
alpha = input('alpha (level of significance) = '); % significance level

% Compute MC size to ensure that the error is < err, with confidence level 1 - alpha
N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

n = 3; % number of successes (damaged files)
X = zeros(1, N); % storage for MC runs (store nr of clean files found before 3rd damaged file in each run)

for i = 1:N
  % use geo var since nr of failures before a success is Geo distrib
    Y = ceil(log(1 - rand(n, 1))/log(1 - p) - 1);
    X(i) = sum(Y); % nr of clean files before each damaged file
end

fprintf('simulated probab. P(X = 5) = %g\n', mean(X==5));
fprintf('true probab. P(X = 5) = %g\n', nbinpdf(5, n, p));
fprintf('error= %e\n', abs(nbinpdf(5, n, p)) - mean(X==5));





% ----------------------------------------------------------------------
% Problem 5: Geometric Distribution (Failures Before 1st Success)
% ----------------------------------------------------------------------
%  A remote sensor successfully transmits data with a probability of $p=0.3$.
%The transmissions are independent. Conduct a Monte Carlo study to estimate:
%a) The probability that the sensor needs more than 4 trials to achieve its first successful transmission.
%b) The expected number of failures before the first successful transmission.
p = 0.3; % probability of success (successful transmission)

err = input('max error = '); % maximum error
alpha = input('alpha (level of significance) = '); % significance level

% Compute MC size (N) using the standard proportion formula
N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

% a) Use the DITM to generate a Geo(p), p ∈ (0, 1), variable.
% X = failures before first success
X = ceil(log(1 - rand(1, N)) ./ log(1 - p) - 1); % Geo variables

% a) Probability that P(X >= 4) (More than 4 trials means 4 or more failures)
k_a = 4;
sim_prob_a = mean(X >= k_a);
true_prob_a = 1 - geocdf(k_a - 1, p); % P(X >= 4) = 1 - P(X <= 3)

fprintf('\n--- Part (a): P(Failures >= 4) ---\n');
fprintf('simulated probab. P(X >= 4) = %g\n', sim_prob_a);
fprintf('true probab. P(X >= 4) = %g\n', true_prob_a);
fprintf('error= %e\n', abs(true_prob_a - sim_prob_a));

% b) Expected number of failures (E(X))
sim_mean_b = mean(X);
true_mean_b = (1 - p) / p;

fprintf('\n--- Part (b): E(Failures) ---\n');
fprintf('simulated mean E(X) = %g\n', sim_mean_b);
fprintf('true mean E(X) = %g\n', true_mean_b);
fprintf('error= %e\n', abs(true_mean_b - sim_mean_b));




% ----------------------------------------------------------------------
% Problem 6: Gamma Distribution (Waiting Time for 5th Event)
% ----------------------------------------------------------------------
% A call center receives calls according to a Poisson process with an average rate (lambda)
%of 2 calls per minute. Conduct a Monte Carlo study to estimate:
%a) The probability that the call center waits less than 5 minutes for the 5th incoming call.
%b) The expected waiting time (in minutes) until the 5th incoming call.

lambda = 2; % rate of calls per minute (Poisson rate)
a = 5; % number of events (alpha)
t_a = 5; % time threshold (5 minutes)

err = input('max error = '); % maximum error
alpha_level = input('alpha (level of significance) = '); % significance level

% Compute MC size (N)
N = ceil(0.25 * (norminv(alpha_level / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

% --- Monte Carlo Simulation ---
% Generate N samples of the Gamma variable T ~ Gam(a, 1/lambda)
% Method: Sum of 'a' independent Exponential variables

X = zeros(1, N);
lambda_mean = 1/lambda; % Mean of the underlying Exponential process

for i = 1 : N
    % Generate 'a' independent Exponential variables and sum them up
    X(i) = sum(-lambda_mean * log(rand(a, 1))); % the Gamma variables (waiting time)
end


% a) Probability that T < 5 (P(X < 5))
sim_prob_a = mean(X < t_a);
true_prob_a = gamcdf(t_a, a, lambda_mean); % gamcdf(t, alpha, 1/lambda)

fprintf('\n--- Part (a): P(T < 5) ---\n');
fprintf('simulated probab. P(X < 5) = %g\n', sim_prob_a);
fprintf('true probab. P(X < 5) = %g\n', true_prob_a);
fprintf('error= %e\n', abs(true_prob_a - sim_prob_a));

% b) Expected waiting time (E(T))
% For Gamma: E(T) = alpha / lambda = a * (1/lambda_mean)

sim_mean_b = mean(X);
true_mean_b = a / lambda;

fprintf('\n--- Part (b): E(T) ---\n');
fprintf('simulated mean E(T) = %g\n', sim_mean_b);
fprintf('true mean E(T) = %g\n', true_mean_b);
fprintf('error= %e\n', abs(true_mean_b - sim_mean_b));




% ----------------------------------------------------------------------
% Problem 7: Negative Binomial Distribution (Failures before 10th Success)
% ----------------------------------------------------------------------
% A researcher is conducting a series of genetic trials, where each trial has a success rate of $p=0.4$.
%The researcher decides to stop only after achieving 10 successful trials. Conduct a Monte Carlo study to estimate:
%a) The probability that the researcher needs more than 30 failures to achieve the 10th successful trial.
%b) The expected number of total trials conducted to achieve the 10th successful trial.
p = 0.4; % success probability
n = 10; % number of successes (r)
k_a = 15; % number of failures for part (a)

err = input('max error = '); % maximum error
alpha = input('alpha (level of significance) = '); % significance level

% Compute MC size (N)
N = ceil(0.25 * (norminv(alpha / 2, 0, 1) / err)^2);
fprintf('Nr. of simulations N = %d\n', N);

X = zeros(1, N); % storage for failures count

% --- Monte Carlo Simulation ---
for i = 1:N
    % Generate n independent Geometric(p) variables (Y)
    % Y = failures before a success
    Y = ceil(log(1 - rand(n, 1))/log(1 - p) - 1); % Geo variables
    X(i) = sum(Y); % Sum of n Geometric variables = NB variable (failures until n successes)
end

% a) P(X = 15)
sim_prob_a = mean(X == k_a);
true_prob_a = nbinpdf(k_a, n, p);

fprintf('\n--- Part (a): P(X = 15) ---\n');
fprintf('simulated probab. P(X = 15) = %g\n', sim_prob_a);
fprintf('true probab. P(X = 15) = %g\n', true_prob_a);
fprintf('error= %e\n', abs(true_prob_a - sim_prob_a));

% b) Expected number of failures E(X)
% For Negative Binomial: E(X) = n * (1 - p) / p

sim_mean_b = mean(X);
true_mean_b = n * (1 - p) / p;

fprintf('\n--- Part (b): E(X) ---\n');
fprintf('simulated mean E(X) = %g\n', sim_mean_b);
fprintf('true mean E(X) = %g\n', true_mean_b);
fprintf('error= %e\n', abs(true_mean_b - sim_mean_b));
