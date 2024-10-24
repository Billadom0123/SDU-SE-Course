hdrs = ipc.h
opts = -g -c
p_src = producer.c ipc.c
p_obj = producer.o ipc.o
a_src = Consumer_A.c ipc.c
a_obj = Consumer_A.o ipc.o
b_src = Consumer_B.c ipc.c
b_obj = Consumer_B.o ipc.o
c_src = Consumer_C.c ipc.c
c_obj = Consumer_C.c ipc.o
all:	Consumer_A	Consumer_B	Consumer_C producer
Consumer_A:		$(a_obj)
	gcc $(a_obj) -o Consumer_A
Consumer_A.o: 	$(a_src) $(hdrs)
	gcc $(opts) $(a_src) -lrt
Consumer_B:		$(b_obj)
	gcc $(b_obj) -o Consumer_B
Consumer_B.o: 	$(b_src) $(hdrs)
	gcc $(opts) $(b_src) -lrt
Consumer_C:		$(c_obj)
	gcc $(c_obj) -o Consumer_C
Consumer_C.o: 	$(c_src) $(hdrs)
	gcc $(opts) $(c_src) -lrt
producer: 	$(p_obj)
	gcc $(p_obj) -o producer
producer.o: 	$(p_src) $(hdrs)
	gcc $(opts) $(p_src) -lrt
clean:
	rm Consumer_A Consumer_B Consumer_C producer *.o 
