region = input('请输入地区索引:  ');

st = 1;

[p,q,r] = size(data);
r_data = zeros(p,r);

for i = 1:p
    for j = 1:r
        r_data(i,j) = data(i,region,j);
    end
end

multi_business_seq(r_data,st,region);