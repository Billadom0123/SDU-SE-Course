import java.util.Random;
public class Experiment3_page_rank {
    int linjiejuzhen[][];//二维数组存放邻接矩阵
    int chubian[];//记录每个点的出边数目
    double rank[];//记录每个点的rank值
    int size;//矩阵的边长	//打印有向邻接矩阵
    public Experiment3_page_rank(int n)
    {
        init(n);
        print();
        countchubian();	}

    public void print()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                System.out.print(linjiejuzhen[i][j]+" ");			}
            System.out.println();		}	}

    public void init(int n)//【i】【j】为0表示无边，为1表示i指向j
    {		//随机生成有向图
        size=n;
        this.linjiejuzhen=new int[size][size];
        Random rand = new Random();
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
            {
                if(i==j)
                    linjiejuzhen[i][j]=0;
                else
                    linjiejuzhen[i][j]=rand.nextInt(2);
            }
        //初始化每个节点的rank值
        rank=new double[size];
        for(int i=0;i<size;i++)
            rank[i]=1.0/size;
    }

    public void countchubian()//统计每个节点有几个出边
    {
        chubian=new int[size];
        for(int i=0;i<size;i++)
        {
            chubian[i]=0;
        }

        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
            {
                if(i!=j&&linjiejuzhen[i][j]==1)
                    chubian[i]++;
            }
    }

    public void rank()
    {
        for(int i=0;i<size;i++)//遍历所有节点
        {
            for(int j=0;j<size;j++)//找出指向当前节点的所有节点，加上rank值
            {
                if(i!=j&&linjiejuzhen[j][i]==1)
                {
                    rank[i]+=rank[j]/chubian[j];
                }
            }
        }
    }

    public void printChubian()
    {
        String r="";
        for(int i=1;i<size+1;i++)
            r+=i+":"+chubian[i-1]+"  ";
        System.out.println("节点出边"+"\n"+r);
    }

    public void printRank()
    {
        String r="";
        for(int i=1;i<size+1;i++)
            r+=i+":"+rank[i-1]+"  ";
        System.out.println("节点rank值"+"\n"+r);
    }


    public static void main(String[] args) {		// TODO Auto-generated method stub
        Experiment3_page_rank temp=new Experiment3_page_rank(4);
        temp.printRank();
        temp.printChubian();
        for(int i=0;i<5;i++)
        {
            System.out.println("第"+(i+1)+"次rank");
            temp.rank();
            temp.printRank();
        }
    }
}