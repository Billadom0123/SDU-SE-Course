function [] = ACF_visual(acf,lags,target_dir)
    % 绘制自相关图  
    stem(lags, acf);  
    xlabel('Lags');  
    ylabel('Autocorrelation');  
    title('Autocorrelation Function');  
    grid on;
end

