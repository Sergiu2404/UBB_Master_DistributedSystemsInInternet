% Approximate Pi by the rejection method, Ex.7.2, Lecture 4.
% Pi is the area of the unit circle X^2 + Y^2 <= 1.
% Cover that area by the rectangle [-1, 1] x [-1, 1].

clear all
err = input('error of the approximation = '); % maximum error
alpha = input('alpha (level of significance) = '); % level of significance
% Generate a sample
% N = input('nr. of simulations = '); % at least 10000
N = ceil(0.25*(norminv(alpha/2, 0, 1)/err)^2); % MC size to ensure that
% the error is < err, with confidence level 1 - alpha
fprintf('Nr. of simulations N = %d \n\n', N)
N_pi = 0; % initial number of pairs that are inside the unit circle.
for j = 1 : N
    X = unifrnd(-1, 1); % U(-1, 1) variable
    Y = unifrnd(-1, 1); % U(-1, 1) variable
    if X^2 + Y^2 <= 1
        N_pi = N_pi + 1; % number of pairs that are inside the unit circle.
    end
end

approx_Pi = 4 * N_pi / N;


% Comparison
fprintf('approximate value of Pi = %1.10f\n', approx_Pi)
fprintf('error = %e\n\n', abs(approx_Pi - pi))