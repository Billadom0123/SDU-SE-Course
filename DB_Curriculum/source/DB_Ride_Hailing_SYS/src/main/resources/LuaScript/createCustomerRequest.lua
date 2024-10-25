local reqId = ARGV[1] -- 请求id
local is_reservation = ARGV[2] -- 是否预约
local is_instead = ARGV[3] -- 是否代叫

redis.call(
    'xadd','customer.request','*',
    'reqId',reqId,
        'is_reservation',is_reservation,
        'is_instead',is_instead
)
return 0