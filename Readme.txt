QCS模拟器环境：
Java/IntelliJ IDEA
Python(需要Qiskit第三方库)/Spyder
Windows 10/处理器：Intel(R) Core(TM) i5-1035G1, RAM: 16GB

QCS支持量子运算指令：

qubit x			声明量子比特x
H(x)			Hadmard门运算
X(x)			Pauli-X门运算
Y(x)			Pauli-Y门运算
Z(x)			Pauli-Z门运算
SDG(x)			S-Dagger门运算（目前只支持电路绘制）
Cnot(x,y)			Cnot门运算，其中x为控制位，y为受控位
CZ(x,y)			Controlled-Z门运算，其中x为控制位，y为受控位
swap(x,y)			SWAP门运算，交换x和y的概率幅（目前只支持电路绘制）
dump			显示模拟器整体量子位概率幅状态
dump x			显示量子比特x概率幅状态
mesure			观测整体量子系统的值
meaure x			观测量子比特x的值
refresh			清空模拟器
partial			设定模拟器量子门运算为局部结果
global			设定模拟器量子门运算为整体结果
circuit_off			关闭量子门电路实时显示
circuit_on			开启量子门电路实时显示
view			查看量子门电路
exit()			退出模拟器

