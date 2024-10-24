hdrs = ipc.h
opts = -g -c
p_src = barber.c ipc.c
p_obj = barber.o ipc.o
c_src = customer.c ipc.c
c_obj = customer.c ipc.o
all:	customer barber
customer:		$(c_obj)
	gcc $(c_obj) -o customer
customer.o: 	$(c_src) $(hdrs)
	gcc $(opts) $(c_src) -lrt
barber: 	$(p_obj)
	gcc $(p_obj) -o barber
barber.o: 	$(p_src) $(hdrs)
	gcc $(opts) $(p_src) -lrt
clean:
	rm customer barber *.o 
