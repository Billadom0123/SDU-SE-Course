function [] = Ljung_Box_Q(ts)
    % 对时间序列进行Ljung-Box Q检验  
    [h, ~, ~, ~] = lbqtest(ts);  

    % 输出检验结果  
    if h == 0  
        disp('没有证据表明序列是自相关的。');  
    else  
        disp('序列可能是自相关的。');  
    end
end

