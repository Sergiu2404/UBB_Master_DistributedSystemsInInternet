% Generate Exp distr. Exp(lambda), using the Inverse Transform method
% i.e. X = -1/lambda * ln(U).
clear all
lambda = input('lambda ( > 0) = '); % the parameter of the Exp distr.
% Generate one variable
% X = -1/lambda*log(rand);

err = input('error ( < lambda) = '); % maximum error
alpha = input('alpha (level of significance) = '); % sign. level
% Generate a sample of such variables
N = ceil(0.25 * (norminv(alpha/2, 0, 1)/err)^2); % MC size to ensure
% that the error is < err, with confidence level 1 - alpha
fprintf('Nr. of simulations N = %d \n\n', N)

X = zeros(1, N);
for i = 1 : N
    X(i) = -1/lambda*log(rand); % the Exp variables
end

% Application/Comparison

fprintf('simulated probab. P(X <= 2) = %1.5f\n', mean(X <= 2))
fprintf('true probab. P(X <= 2) = %1.5f\n', expcdf(2, 1/lambda))
fprintf('error = %e\n\n', abs(expcdf(2, 1/lambda) - mean(X <= 2)))

fprintf('simulated probab. P(X < 2) = %1.5f\n', mean(X < 2))
fprintf('true probab. P(X < 2) = %1.5f\n', expcdf(2, 1/lambda))
fprintf('error = %e\n\n', abs(expcdf(2, 1/lambda) - mean(X < 2)))

fprintf('simulated mean E(X) = %5.5f\n', mean(X))
fprintf('true mean E(X) = %5.5f\n', 1/lambda)
fprintf('error = %e\n\n', abs(1/lambda - mean(X)))
