% 5) Messages arrive at an electronic mail server according to a Poisson process with the average frequency
%of 5 messages per minute. The server can process only one message at a time and messages
%are processed on a “first come − first serve” basis. It takes an Exponential amount of time M1 to
%process any text message, plus an Exponential amount of time M2, independent of M1, to process
%attachments (if there are any), with E(M1) = 2 seconds and E(M2) = 7 seconds. Forty percent of
%messages contain attachments. Use Monte Carlo methods to estimate
% a) the expected response time of this server;
% b) the expected waiting time of a message before it is processed.
% For each job,
%response time = departure time − arrival time,
%waiting time = service starting time − arrival time,
%so we will keep track of arrival times, of times when service starts and times when service finishes
%(departure times), as arrays. In addition, we need to know when the server becomes available, so
%we will have a variable for that time. We start with the parameters and initialization of variables
lamA = 5 / 60;
lamM1 = 1 / 2;
lamM2 = 1 / 7;
p = 0.4; % proportion with mails with attachemnts

N = input('size of MC study: '); % nr of generated jobs
% each job time in below arrays
arrival = zeros(1, N);
start = zeros(1, N);
finish = zeros(1, N);

T = 0; % arrival time of new job
A = 0; % time when server becomes available

% Then each new arrival time is an Exp(λA) variable. For service time, it isM1 ∈ Exp(λM1) plus the
%service time for attachmentsM2 ∈ Exp(λM2), with probability p = 0.4. Whether or not we add the
%time for processing attachments is a Bernoulli variable with parameter p = 0.4
% Remember that this is simulated as U < p (“success”), for a Standard Uniform variable U. So the
% total service time will be M1 + (U < p) ∗ M2. For each job, the service starts either when the job
% arrives or when the server becomes available, whichever happens last.

for j = 1 : N % N jobs
  % generate arrival time of the j-th job (exp interarriv time added to prev arriv time)
  T = T - 1/lamA * log(rand);
  % service time = time to process M1 + time to process M2 (of probab 0.4)
  S = -1/lamM1 * log(rand) - (rand < p) * 1/lamM2 * log(rand);
  arrival(j) = T; % arrival time of the j-th job
  start(j) = max(A, T); % time when service starts
  finish(j) = start(j) + S; % departure time
  A = finish(j); % time when the server becomes available
  % to take the (j+1)st job
end
% el_time = cputime - t; % elapsed time, if you want
% to compute the CPU time

fprintf('a) expected response time E(R) is %3.5f sec.\n', mean(finish - arrival));
fprintf('b) expected waiting time E(W) is %3.5f sec.\n', mean(start - arrival))
% fprintf(’CPU time = %f seconds\n’, el_time)
