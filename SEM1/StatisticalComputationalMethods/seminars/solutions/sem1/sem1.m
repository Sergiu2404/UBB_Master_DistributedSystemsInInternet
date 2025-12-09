pkg load statistics
% 2) Messages arrive at an electronic message center at random times, with an average of 9 messages
% per hour. What is the probability of
% a) receiving exactly 5 messages during the next hour (event A)?
% b) receiving at least 5 messages during the next hour (event B)?
lambda = 9;
%a)
P_A = poisspdf(5, lambda);
%b)
P_B = poisscdf(4, lambda);


% 3) After a computer virus entered the system, a computer manager checks the condition of all important
%files. He knows that each file has probability 0.2 to be damaged by the virus, independently
%of other files. Find the probability that
%a) at least 5 of the first 20 files checked, are damaged (event A);
%b) the manager has to check at least 6 files in order to find 3 that are undamaged (event B).
n = 20;
p = 0.2;
%a) P(A) = P(X>=5) = 1 - P(X<=4)
P_A = 1 - binocdf(4, 20, 0.2)
%b) undamaged => p = 0.8;
% at leats 6 trials for 3 succ / 3rd succ in at least 6 trials / 3 succ after at least 3 fails;
% P(X>=3) = 1 - P(X<=2), NEGATIVE BINO bc nr of trials is not fixed
p = 0.8;
P_B = 1 - nbincdf(2, 3, 0.8)


% 4) Consider a satellite whose work is based on block A, independently backed up by a block B. The
% satellite performs its task until both blocks A and B fail. The lifetimes of A and B are Exponentially
% distributed with mean lifetime of 10 years. What is the probability that the satellite will work for
% more than 10 years (event E)?
% EXPONENTIAL DISTIRBUTIONS: same as geometric, but continuous (geometric is used for discrete)
% exp distributed with mean = 10 years => E(Ta) = E(Tb) = 10 years
% works > 10 years if: E = {Ta > 10 or Tb > 10}, bc if A fails, B could still handle it
% P((Ta>10) U (Tb>10)) = 1 - P((Ta<=10) intersect (Tb<=10))
% they are independend => 1 - P(Ta<=10) * P(Tb<=10)
mu = 10; % mean lifetime of A and B
P_fail = expcdf(10, mu); % P(T<=10), probab that one fails in 10 years
P_E = 1 - P_fail^2 %P(E), 1 - both fail in 10 years => probab at least one survives more than 10 years


% 5) Compilation of a computer program consists of 3 blocks that are processed sequentially, one after
% the other. Each block takes Exponential time with the mean of 5 minutes, independently of other
% blocks. Compute the probability that the entire program is compiled in less than 12 minutes (event
% A). Use the Gamma-Poisson formula to compute this probability two ways.
% Gamma: sum of k indep exponential vars is Gamma distrib (time until kth event is Gamma)
% Poisson: nr of events in time t is Poisson
% T = total compile time => T is sum of 3 indep Exponential vars with lambda = 1/5 =>
% => Gamma distribution with alpha = 3, 1/lambda = 5
% => Direct Way: P(T<12) = gamcdf(12, 3, 5)
P_A = gamcdf(12, 3, 5)

% OR Gamma Poisson: P(T<=t), P(X>=alpha), where X has Poisson formula with param lambda * t = 1/5 * 12 = 2.4
% Since t is cont random var, P(T<12) = P(T<=12) => P(X>=3) = 1 - P(X<=2)
% P_A = 1 - poisscdf(2, 2.4)


% 6) Under good weather conditions, 80% of flights arrive on time. During bad weather, only 50% of
% flights arrive on time. Tomorrow, the chance of good weather is 60%. What is the probability that
% your flight will arrive on time?
% events: A = flight arrives on time, G = good weather
% P(A|G) = 0.8, P(A|!G) = 0.5, P(G) = 0.6, P(!G) = 1 - 0.6 = 0.4, P(A) = ?
% Sum of scenarios (good weather + bad weather) =
% = probab of arrive on time if good weather weighted by chance of good weather + probab of arrive on time if bad weather weighted by chance bad weather
% P(A) = P(A|G) * P(G) + P(A|!G) * P(!G)
P_AG = 0.8; P_G = 0.6;
P_AnG = 0.5; P_nG = 0.4;

P_A = P_AG * P_G + P_AnG * P_nG

