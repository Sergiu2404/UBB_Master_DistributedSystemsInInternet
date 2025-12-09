function X = poisson_sim(lambda, samples)

X = zeros(1, samples);

for i = 1:samples
    count = 0;
    current_time = 0;

    while true
        T_i = -log(rand(1)) / lambda;

        if (current_time + T_i) < 1.0
            current_time = current_time + T_i;
            count = count + 1;
        else
            break;
        endif
    endwhile

    X(i) = count;
endfor

endfunction
