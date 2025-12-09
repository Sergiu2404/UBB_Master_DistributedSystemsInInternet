% Generate Poisson distr. P(lambda), using a special method
% (related to the Exp distr.).
clear all
lambda = input('lambda ( > 0) = '); % param. of the Poisson distr.
% Generate one variable
% U = rand; % generated U(0,1) variable
% X = 0; % initial value
% while U >= exp(-lambda) % check that U1*...*Un >= exp(-lambda),
%     % to get the max n
%     U = U * rand; % go further to n + 1 (i.e. X + 1)
%     X = X + 1; % the Poisson variable
% end

% Generate a sample of such variables
err = input('error = '); % maximum error
alpha = input('alpha (level of significance) = '); % level of significance
N = ceil(0.25*(norminv(alpha/2, 0, 1)/err)^2); % MC size to ensure that
% the error is < err, with confidence level 1 - alpha
fprintf('Nr. of simulations N = %d \n\n', N)
X = zeros(1, N);
for i = 1 : N
    U = rand; % generated U(0,1) variable
    while U >= exp(-lambda) % check that U1*U2*...*Uk >= exp(-lambda), to
        % get the max n for which that happens
        U = U * rand; % go further to n + 1 (i.e. X + 1)
        X(i) = X(i) + 1; % the Poisson variable
    end
end

% Application/Comparison

fprintf('simulated probab. P(X = 2) = %1.5f\n', mean(X == 2))
fprintf('true probab. P(X = 2) = %1.5f\n', poisspdf(2, lambda))
fprintf('error = %e\n\n', abs(poisspdf(2, lambda) - mean(X == 2)))

fprintf('simulated probab. P(X <= 2) = %1.5f\n', mean(X <= 2))
fprintf('true probab. P(X <= 2) = %1.5f\n', poisscdf(2, lambda))
fprintf('error = %e\n\n', abs(poisscdf(2, lambda) - mean(X <= 2)))

fprintf('simulated probab. P(X < 2) = %1.5f\n', mean(X < 2))
fprintf('true probab. P(X < 2) = %1.5f\n', poisscdf(1, lambda))
fprintf('error = %e\n\n', abs(poisscdf(1, lambda) - mean(X < 2)))

fprintf('simulated mean E(X) = %5.5f\n', mean(X))
fprintf('true mean E(X) = %5.5f\n', lambda)
fprintf('error = %e\n\n', abs(lambda - mean(X)))
