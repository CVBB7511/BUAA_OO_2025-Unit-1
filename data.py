import random 
import tempfile
intPool = [0,1,2,3,4,5,1145141919810]
PoolSize = len(intPool)
LeadZero = False
maxTerm = 3
maxFactor = 2
maxceng = 3 #表达式嵌套层数
f0 = "f{0}(y,x)=x - y"
f1 = "f{1}(y,x)=sin(x)-sin(y)"
fn = "f{n}(y,x)=2*f{n-1}(y^2,x)-3*f{n-2}(sin(y),x)"
noF = [0,1,3,4]
def rd(a,b):
    return random.randint(a,b)

def rd_in(input_list):
    length = len(input_list)
    index = rd(0,length-1)
    return input_list[index]

def getFactor(restceng, Ft):
    #剩余可用层数 Ft F的最大嵌套次数
    # 0 常数
    # 1 幂函数
    # 2 函数调用 f{index}(Factor1,Factor2) 最多嵌套一层
    # 3 三角函数 sin(Factor)
    # 4 表达式
    result = "("
    
    if (restceng <= 0):
        type = rd(0,1)
    elif (Ft >= 2):
        type = rd_in(noF)
    else:
        type = rd(0,4)
    
    if (type == 0):
        index = rd(0,PoolSize - 1)
        sign = rd (0,1);
        if (sign == 1):
            result = result + "+"
        else:
            result = result + "-"
        if (LeadZero):
            isZero = rd(0,1)
            if (isZero == 1):
                result = result + "0"
        result = result + str(intPool[index])
        
    if (type == 1):
        result = result + "x"
        power = rd(0,3)
        if (power != 1):
            result = result + "^" + str(power)
    
    if (type == 2):
        index = rd(0,5)
        result = result +"f{" + str(index) + "}" + "("
        factor1 = getFactor(restceng - 1, Ft + 1)
        factor2 = getFactor(restceng - 1, Ft + 1)
        result = result + factor1 + "," + factor2 + ')'
        
    if (type == 3):
        trigType = rd(0,1)
        if (trigType == 0):
            result = result + "sin("
        else:
            result = result + "cos("
        factor = getFactor(restceng - 1, Ft)
        result = result + factor + ")"
    if (type == 4):
        ispow = rd(0,1)
        if (ispow == 0):
            result = result + getExpr(restceng - 1,Ft)
        else:
            result = result + "(" +  getExpr(restceng - 1,Ft) + ")^" + str(rd(0,2)) 
    result = result + ")"
    return result

def getsign():
    type = rd(0,1)
    if (type == 0):
        return "+"
    else:
        return "-"
    
def getTerm(restceng,Ft):
    factorNum = rd(0,maxFactor)
    signNum = rd(0,1)
    result = "("
    for i in range(signNum):
        result = result + getsign()
    result = result + getFactor(restceng-1,Ft)
    for i in range(0,factorNum):
        result = result + "*" + getFactor(restceng-1,Ft)
    result = result + ")" 
    return result

def getExpr(restceng,Ft):
    TermNum = rd(0,maxTerm)
    signNum = rd(0,1)
    result = "("
    for i in range(signNum):
        result = result + getsign()
    result = result + getTerm(restceng-1,Ft)
    for i in range(0,TermNum):
        isplus = rd(0,1)
        if (isplus == 1):
            result = result + "+" + getTerm(restceng-1,Ft)
        else:
            result = result + "-" + getTerm(restceng-1,Ft)
    result = result + ")"
    return result

def start():
    str = getExpr(maxceng,0)
    print(str)
    lines = ["1",f1,fn,f0,str];
    with open('1.txt', 'w', encoding='utf-8') as file:
        file.write('\n'.join(lines))

def main():
    print("1")
    print(f1)
    print(fn)
    print(f0)
    str = getExpr(maxceng,0)
    print(str)

if __name__ == "__main__":
    main()