
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
    //��������ĳ��ȣ�����������
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
    //������ͷ����һ�����
    public void addANodeToStart(String addData)
    {
        head = new ListNode(addData, head);
    }
    //������ͷɾ��һ�����
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
    //��������target�Ƿ����������������ݲ��֣�������Ӧ�����ã�δ�ҵ�����null
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
    //������������Ļ����ʽ��������ڸ����������ݡ��������ǰ�����е���������Ϊ
    // ͷ���->"One"->"Two"->"Three"->null
    // ��������� "Three"->"Two"->"One"
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
