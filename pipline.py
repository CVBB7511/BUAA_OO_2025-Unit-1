import sympy # 如果报错显示没有这个包，就需要导入
from xeger import Xeger
import random
import subprocess
from subprocess import STDOUT, PIPE
from data import start


def execute_java(str):
    with open("1.txt", 'r', encoding='utf-8') as txt_file:
        input_data = txt_file.read()
    cmd = ['java', '-jar', str,"1.txt"]# 更改为自己的.jar包名
    proc = subprocess.Popen(cmd, 
                            stdin=PIPE, 
                            stdout=PIPE, 
                            stderr=STDOUT,
                            text = True)
    stdout, stderr = proc.communicate(input = input_data)
    return stdout.strip()


x = sympy.Symbol('x')
X = Xeger(limit=10)
cnt = 0
while (1):
    cnt = cnt + 1
    if cnt % 10 == 0:
        print(cnt)
    poly = start()
    myout = execute_java("1_.jar")
    print("myout_finish="+myout)
    yourout = execute_java("2_.jar")
    print("yourout_finish="+yourout)
    myout = sympy.simplify(myout)
    yourout = sympy.simplify(yourout)
    
    if sympy.simplify(myout-yourout) == 0:
        print("E")
    else:
        print("NE")
        #print(poly)
        print(myout)
        print(yourout)
        break
    # #print(strr)
    # g = sympy.parse_expr(strr)
    # if sympy.simplify(f).equals(g) :
    #     print("AC : " + str(cnt))
    # else:
    #     print("!!WA!! with " + "poly : " + poly + " YOURS: " + strr)
