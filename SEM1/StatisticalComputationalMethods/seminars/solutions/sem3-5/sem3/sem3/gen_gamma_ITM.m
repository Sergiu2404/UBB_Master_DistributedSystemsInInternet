% Generate Gamma distr. Gamma(a, lambda), a in N, using the Inverse
% Transform method for "a" Exp(1/lambda) variables and adding them
clear all
a = input('"a" (positive integer) = '); % the 1st parameter of Gamma distr.
lambda = input('lambda ( > 0) = '); % the 2nd parameter of Gamma distr.
% Generate one variable
% X = sum(-lambda*log(rand(a, 1))); % the gamma variable

err = input('error ( < lambda) = '); % maximum error
alpha = input('alpha (level of significance) = '); % level of significance
% Generate a sample of such variables
N = ceil(0.25*(norminv(alpha/2, 0, 1)/err)^2); % MC size to ensure that
% the error is < err, with confidence level 1 - alpha
fprintf('Nr. of simulations N = %d \n\n', N)
X = zeros(1, N);
for i = 1 : N
    X(i) = sum(-lambda*log(rand(a, 1))); % the Gamma variables
end

% Application/Comparison

fprintf('simulated probab. P(X <= 2) = %1.5f\n', mean(X <= 2))
fprintf('true probab. P(X <= 2) = %1.5f\n', gamcdf(2, a, lambda))
fprintf('error = %e\n\n', abs(gamcdf(2, a, lambda) - mean(X <= 2)))

fprintf('simulated probab. P(X < 2) = %1.5f\n', mean(X < 2))
fprintf('true probab. P(X < 2) = %1.5f\n', gamcdf(2, a, lambda))
fprintf('error = %e\n\n', abs(gamcdf(2, a, lambda) - mean(X < 2)))

fprintf('simulated mean E(X) = %5.5f\n', mean(X))
fprintf('true mean E(X) = %5.5f\n', a * lambda)
fprintf('error = %e\n\n', abs(a * lambda - mean(X)))
