
public class ThreeTwo {
	public class ListNode
	{
	    private String data;
	    private ListNode link;
	    public ListNode(String newData, ListNode linkValue)
	    {
	        data = newData;
	        link = linkValue;
	    }
	}
    private ListNode head;

    public ThreeTwo( )
    {
        head = null;
    }
    //计算链表的长度，返回链表长度
    public int length( )
    {
        int count = 0;
        ListNode position = head;
        while (position != null)
        {
            count++;
            position = position.link;
        }
        return count;
    }
    //在链表头加入一个结点
    public void addANodeToStart(String addData)
    {
        head = new ListNode(addData, head);
    }
    //在链表头删除一个结点
    public void deleteHeadNode( )
    {
        if (head != null)
        {
            head = head.link;
        }
        else
        {
            System.out.println("Deleting from an empty list.");
            System.exit(0);
        }
    }
    public boolean onList(String target)
    {
        return (Find(target) != null);
    }
    //查找数据target是否存在于链表结点的数据部分，返回相应的引用，未找到返回null
    private ListNode Find(String target)
    {
        ListNode position;
        position = head;
        String dataAtPosition;
        while (position != null)
        {
            dataAtPosition = position.data;
            if (dataAtPosition.equals(target))
                return position;
            position = position.link;
        }
        return null; 
    }
    //反向依次向屏幕按格式输出链表内各个结点的数据。即如果当前链表中的数据内容为
    // 头结点->"One"->"Two"->"Three"->null
    // 请依次输出 "Three"->"Two"->"One"
    public void ReversedshowList()
    {
    	
    }
    public static void main(String[] args)
    {
    	ThreeTwo list = new ThreeTwo( );
        list.addANodeToStart("One");
        list.addANodeToStart("Two");
        list.addANodeToStart("Three");
        System.out.println("List has " + list.length( ) 
                            + " entries.");
        list.showList( );

        if (list.onList("Three"))
            System.out.println("Three is on list.");
        else
            System.out.println("Three is NOT on list.");

        list.deleteHeadNode( );

        if (list.onList("Three"))
            System.out.println("Three is on list.");
        else
            System.out.println("Three is NOT on list.");

        list.deleteHeadNode( );
        list.deleteHeadNode( );
        System.out.println("Start of list:");
        list.showList( );
        System.out.println("End of list.");
    }
}
