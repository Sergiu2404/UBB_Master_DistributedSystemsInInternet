pkg load statistics
% Messages arrive at an electronic message center at random times, with an average of 9 messages
% per hour. What is the probability of
% a) receiving exactly 5 messages during the next hour (event A)?
% b) receiving at least 5 messages during the next hour (event B)?
lambda = 9;
%a)
P_A = poisspdf(5, lambda);
%b)
P_B = poisscdf(4, lambda);


%After a computer virus entered the system, a computer manager checks the condition of all important
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



