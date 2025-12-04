% Simulate Markov chain in Problem 4, Seminar 4.
clear all
Nm = 30;          % length of sample path (of Markov chain)
N = input('nr. of simulations = ');
maxs = zeros(1, N);
maxr = zeros(1, N);
for j= 1:N

    P0 = [0.2 0.8];   % initial distr. of sunny/rainy
    P  = [0.7 0.3; 0.4 0.6];  % trans. prob. matrix
    X = zeros(1, Nm); % allocate memory for x
    
    P1(1, :) = P0; % P1 will contain the forecast at each step;
                   % first row contains the forecast in day t = 1
    for t = 1:Nm
        U = rand;
        X(t) = 1 * (U < P0(1)) + 2 * (U >= P0(1));
                % simulate X(1), ... X(Nm) sequentially,
                % as Bernoulli variables taking value 1 with
                % prob. P0(1) and value 2 with prob. 1 - P0(1)
    
        P1(t + 1, :) = P1(t, :) * P; % forecast for next day
    
        P0 = P(X(t), :);% updated vector P0;
                        % prepare the distribution of X(t + 1);
                        % its pdf is the (X(t))th row of matrix P
    
    end
    %X
    
    %P1
    
    
    i_change = [find(X(1 : end-1) ~= X(2:end)), Nm]; %indexes where states change
    
    longstr(1) = i_change(1);     % first long streak ends at
                                 % the first change
    for i = 2 : length(i_change)
        longstr(i) = i_change(i) - i_change(i - 1);
            % the remaining long streaks are differences
            % between any two changes
    end
    
    if (X(1) == 1)      % the first day is sunny
        sunny = longstr(1 : 2 : end);   % long streaks of sunny days
        rainy = longstr(2 : 2 : end);   % long streaks of rainy days
    else                % the first day is rainy
        sunny = longstr(2 : 2 : end);   % long streaks of sunny days
        rainy = longstr(1 : 2 : end);   % long streaks of rainy days
    end
    
    maxs(j) = max(sunny);   % longest streak of sunny days
    maxr(j) = max(rainy);   % longest streak of rainy days
end

% maxs;
% maxr;
fprintf('\n prob. of drought is %1.4f\n', mean(maxs >= 7))
fprintf(' prob. of flooding is %1.4f\n\n', mean(maxr >= 7))


                   
