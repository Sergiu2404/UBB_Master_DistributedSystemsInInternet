pkg load statistics;
% 1)
P = [0.4 0.6;
     0.6 0.4];
% a)
% System is in Mode I at 5:30 pm.
% What is P(in Mode I at 8:30 pm) ?
% Time difference = 3 hours → compute P^3
P3 = P^3;
P_830 = P3(1,1)
fprintf("a) Probability system is in Mode I at 8:30 pm = %.4f\n", P_830);

%% Part (b)
% Long-run (steady-state) distribution
% Solve π * P = π, with π1 + π2 = 1
% Method: solve (P' - I)*pi = 0 with sum(pi)=1
A = [P' - eye(2); 1 1];
b = [0; 0; 1];
pi = A \ b  % steady-state distribution
fprintf("b) Steady-state distribution = [%.4f  %.4f]\n", pi(1), pi(2));


% 2)
P = [0.6 0.4;
     0.3 0.7];
% a) third light (two steps) given first is green
P2 = P^2;
prob_third_red = P2(1,2);
fprintf('a) P(third is red) = %.4f\n', prob_third_red);

% (b) steady-state distribution
% Solve (P' - I)' * pi' = 0 with sum constraint, or use eigenvector
[V,D] = eig(P');
% find eigenvector corresponding to eigenvalue 1
idx = find(abs(diag(D)-1) < 1e-10, 1);
pi = V(:,idx) / sum(V(:,idx));
pi = real(pi)';  % row vector
fprintf('b) steady-state pi = [%.4f  %.4f]\n', pi(1), pi(2));
fprintf('   long-run prob(red) = %.4f\n', pi(2));

% (optional) quick Monte Carlo simulation check
Nsim = 1e5;
states = zeros(1,Nsim);
s = 1; % start green
for i = 1:Nsim
    % advance many steps (e.g. 100) to reach stationarity, then sample
    for k = 1:100
        s = find(rand <= cumsum(P(s,:)),1);
    end
    states(i) = s;
end
fprintf('simulated long-run prob(red) ≈ %.4f\n', mean(states==2));



% 3)
% Part (a) - Transition probability matrix
% Probabilities:
p_disconnect = 0.5;  % probability a connected user disconnects
p_connect    = 0.2;  % probability a disconnected user connects

% Row 1: X0 = 0 (no users connected) -> number of new connections ~ Binomial(2,0.2)
Prow1 = binopdf(0:2, 2, p_connect);  % [p00 p01 p02]

% Row 2: X0 = 1 (1 user connected, 1 disconnected)
% p10 = connected disconnects * disconnected does not connect
p10 = p_disconnect * (1 - p_connect);
% p11 = (connected stays & disconnected stays) OR (connected disconnects & disconnected connects)
p11 = (1 - p_disconnect)*(1 - p_connect) + (p_disconnect*p_connect);
% p12 = connected stays & disconnected connects
p12 = (1 - p_disconnect)*p_connect;
Prow2 = [p10 p11 p12];

% Row 3: X0 = 2 (both users connected) -> number of disconnections ~ Binomial(2,0.5)
Prow3 = binopdf(2:-1:0, 2, p_disconnect);  % [p20 p21 p22]

% Transition matrix
P = [Prow1; Prow2; Prow3];
disp('Transition probability matrix P ='); disp(P);

%% Part (b) - Probability 1 user connected at 10:02 if 2 connected at 10:00
P0 = [0 0 1];  % initial state (2 users connected)
P2 = P0 * P^2; % after 2 minutes
prob_1user = P2(2);
fprintf('Probability 1 user connected at 10:02 = %.4f\n', prob_1user);

% Part (c) - Steady-state distribution and expected number of connections
% Solve π*P = π, π0 + π1 + π2 = 1
A = [P' - eye(3); 1 1 1];
b = [0;0;0;1];
pi = A\b;  % steady-state distribution
fprintf('Steady-state distribution π = [%.4f %.4f %.4f]\n', pi(1), pi(2), pi(3));

% Expected number of connections in steady state
E_connections = sum([0 1 2].*pi');
fprintf('Expected number of connections by noon = %.4f\n', E_connections);






