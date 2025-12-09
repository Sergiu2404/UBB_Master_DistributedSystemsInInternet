% Probl. 4, Sem. 3, Forecasting errors in new software release.
clear all
err = input('error = '); % maximum error
alpha = input('alpha (level of significance) = '); % level of significance
% Generate a sample of variables
N = ceil(0.25*(norminv(alpha/2, 0, 1)/err)^2); % MC size to ensure that
% the error is < err, with confidence level 1 - alpha
fprintf('Nr. of simulations N = %d \n', N)

kdays = input('number of previous days considered = ');
k = kdays;
inlast = input('numbers of errors in the last kdays days (vector of length kdays) = ');
% initial number of errors in the last kdays days
tmax = input('max time after which the new software is withdrawn (in days) = ');

Ttotal = zeros(1, N);
Ntotalerr = zeros(1, N);
% Ttotal is the time it takes to find all the errors (in days)
% Ntotalerr is the total number of errors that are detected
for j = 1 : N
    % T is time from now on (in days), X is nr. of errors on day T
    % nrerr is the number of errors detected so far
    T = 0;
    last = inlast; % number of errors in the last kdays days
    X = inlast(k); % nr. of errors in day T
    nrerr = sum(inlast);
    
    while X > 0 % while loop until no errors are found
        lambda = min(last); % parameter for Poisson var. X
        % Simulate the number of errors on day T, Poisson (lambda), special method
        U = rand; % generated U(0,1) variable
        X = 0; % initial value

        while U >= exp(-lambda) % check that U1*U2*...*Uk >= exp(-lambda), to
            % get the max k for which that happens
            U = U * rand; % go further to k + 1 (i.e. X + 1)
            X = X + 1; % the Poisson variable
        end

        T = T + 1; % next day
        nrerr = nrerr + X; % new nr. of errors
        last = [last(2:k), X]; % new nrs of errors in the last k days
    end % the while loop ends when X = 0 on day T, that means that all errors were found on previous day, T - 1
    
    Ttotal(j) = T - 1; % the day all errors were found
    Ntotalerr(j) = nrerr;
end
fprintf('\n')
fprintf('a) The time it will take to find all errors is %3.3f days \n', mean(Ttotal))
fprintf('b) Total number of errors in the new release is %5.3f \n', mean(Ntotalerr))
fprintf('c) Prob. that some errors will still be undetected after %d days, \n', tmax)
fprintf('after which the software will be withdrawn is %3.3f \n', mean(Ttotal > tmax))
fprintf('\n')
