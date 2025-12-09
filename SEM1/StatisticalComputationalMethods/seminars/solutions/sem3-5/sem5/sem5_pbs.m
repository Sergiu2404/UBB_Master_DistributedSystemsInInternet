pkg load statistics
% SEM5 pb
% 1)
lambda = 6;
p_max = 0.1; % ladning prob

% a)
frames_per_min = lambda / p_max;
delta_min = 1 / frames_per_min;
delta_sec = delta_min * 60;

fprintf('--- Part (a): Frame Length Determination ---\n');
fprintf('Frame Length (Delta t): %d seconds\n', delta_sec);

% b) P(no landings) during the next half a minute
p = p_max;
Delta_t = delta_min; % 1/60 minutes

T_b_min = 0.5; % Time period (half a minute)
% Calculate N: Total number of frames in the period
N_b = T_b_min / Delta_t;
k_b = 0; % Number of successes (landings) we want

% P(X=0) for B(30, 0.1)
P_b = binopdf(k_b, N_b, p);

fprintf('\n--- Part (b): Probability of No Landings ---\n');
fprintf('Trials (N): %d\n', N_b);
fprintf('P(X = 0) = binopdf(0, %d, %.1f)\n', N_b, p);
fprintf('Result: %5.5f\n', P_b);

% c): P(more than 170 landed airplanes) during the next 30 minutes
T_c_min = 30; % Time period (30 minutes)
% Calculate N: Total number of frames in the period
N_c = T_c_min / Delta_t;
k_c = 170; % Boundary for the probability P(X > 170)

% P(X > 170) = 1 - P(X <= 170)
% Use Binomial Cumulative Distribution Function (CDF)
P_c = 1 - binocdf(k_c, N_c, p);

fprintf('\n--- Part (c): Probability of More Than 170 Landings ---\n');
fprintf('Trials (N): %d\n', N_c);
fprintf('P(X > 170) = 1 - binocdf(%d, %d, %.1f)\n', k_c, N_c, p);
fprintf('Result: %5.5f\n', P_c);



% 2)
lambda_hr = 40;
lambda_min = lambda_hr / 60; % 40 messages / hour => 40/60 messages per min
% 30 frames per min => 1 minute per frame
delta = 1 / 30;
t = 30; % min from 10:00 to 10:30
% prob succ in one frame
p = lambda_min * delta;
n = t / delta;

E_X = n * p;
V_X = n * p * (1 - p);
sigma_X = sqrt(V_X);

fprintf('Frame Length (Delta): %f min (1/30)\n', delta);
fprintf('Probability of arrival (p): %f (1/45)\n', p);
fprintf('Total Trials (n): %d\n', n);

fprintf('\nExpected number of messages E(X) = n*p:\n');
fprintf('E(X) = %d messages\n', E_X);

fprintf('\nStandard Deviation sigma(X) = sqrt(n*p*(1-p)):\n');
fprintf('sigma(X) = %5.4f messages\n', sigma_X);


% 3)
lambda_min = 5; % 5 customers / min
% a) P(no offer in 2 minutes) ?
% no offer made i fewer than 3 connections in 2 minutes, t=2 mins, lambda = 5 / min => poisson param = 10
% P(no offer in 2 minutes) = P(x < 3) = P(x <= 2)
t = 2;
lambda = lambda_min * t;
p_nooffer = poisscdf(2, 10)

% b) P(no customers connect for 20 seconds)
% time between connections (interarrival time) has Exp distrib
% no cust connect for 20 sec = 1/3 mins if the interarrival time exceeds that
% P(T > 1/3) = 1 - P(T <= 1/3) = 1 - expcdf(1/3, 1/5)
t = 1/3;
interarrival = 1/5; % 5 cust per min => 1 min has 5 cust => 1 cust per 0,2 min
p_nocust = 1 - expcdf(t, interarrival)
% or in seconds: lambda = 5/60 sec = 1/12 =>
p_nocust = 1 - expcdf(20, 12)
% or since we want 0 connecitons in t = 20 sec = 1/3 mins => lambda = 5 * 1/3 = 5/3
% => compute P(x = 0) = poisscdf(0, 5/3)
p_nocust = poisscdf(0, 5/3)


% c) expectation and std dev of time at first offer = ?
% time T3 of the third connection (arrival) (and therefore, the first offer) is the sum of 3 independent
% Exp(5) times, so it has Gamma distribution with parameters α = 3 and λ = 1/5. Then
% E(T3) = αλ = 3/5 = 0.6 min,
% sigma = sqrt(V (T3)) = sqrt(α * λ2) = 0.3464 min
alpha = 3;
lambda = 5;
E = alpha / lambda
V = alpha / (lambda^2)
sigma = sqrt(V)
