function [acf,lags] = ACF(data,st,et,business,region)
    ts = zeros(1,et-st+1);
    for i = 1:size(ts)
        ts(i) = data(business,region,st+i-1);
    end
    
    Ljung_Box_Q(ts)
    
    [acf,lags] = xcorr(ts,'unbiased');
end

