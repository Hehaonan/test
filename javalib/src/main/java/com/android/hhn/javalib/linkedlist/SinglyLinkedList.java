package com.android.hhn.javalib.linkedlist;

/**
 * Author: haonan.he ;<p/>
 * Date: 2020/4/21,7:13 PM ;<p/>
 * Description: 单链表练习;<p/>
 * Other: ;
 */
public class SinglyLinkedList {

    public static class Node {
        public String data;
        public int value;
        public Node next;

        public Node(String data, Node next) {
            this.data = data;
            this.next = next;
            this.value = data.isEmpty() ? 0 : data.charAt(0);
        }

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 打印链表
     *
     * @param head
     */
    private static void printLinkedList(Node head) {
        if (head == null) {
            System.out.println("链表为空");
            return;
        }
        while (head != null) {
            System.out.print(head.data + "(" + head.value + ")" + "->");
            head = head.next;
        }
        System.out.println();
    }

    /**
     * 打印节点
     *
     * @param node
     */
    private static void printNode(Node node) {
        if (node == null) {
            System.out.println("节点为空");
            return;
        }
        System.out.println(node.data + "(" + node.value + ")");
    }

    /**
     * 通过 data 查找 Node
     *
     * @param head
     * @param data
     *
     * @return
     */
    private static Node findByValue(Node head, String data) {
        while (head != null && head.data != data) {
            head = head.next;
        }
        return head;
    }

    /**
     * 通过位置查找
     *
     * @param head
     * @param index
     *
     * @return
     */
    private static Node findByIndex(Node head, int index) {
        int tempIndex = 0;
        while (head != null && tempIndex != index) {
            head = head.next;
            ++tempIndex;
        }
        return head;
    }

    /**
     * 链表头部插入
     *
     * @param head
     * @param newNode
     *
     * @return
     */
    private static Node insertToHead(Node head, Node newNode) {
        if (head == null) { // 空链表
            head = newNode;
        } else {
            newNode.next = head;// newNode指向head结点
            head = newNode; // head 变成 newNode
        }
        return head;
    }

    /**
     * 链表尾部插入
     *
     * @param head
     * @param newNode
     */
    private static Node insertToTail(Node head, Node newNode) {
        if (head == null) { // 空链表
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;// 尾结点指向newNode
        }
        return head;
    }

    /**
     * 在目标节点前、后添加新节点
     * 先根据target找到真正的操作节点，先后插，在前插
     *
     * @param head    链表头结点
     * @param target  目标节点（用于找到节点不是真正链表中的节点，可以直接传值）
     * @param newNode 新的节点
     *
     * @return
     */
    private static Node insertBeforeAndAfter(Node head, Node target, Node newNode) {
        if (target == null || newNode == null) {
            System.out.println("参数不合法啊");
            return null;
        }
        if (head == null) {
            head = newNode;
        } else if (head.data.equals(target.data)) { // head结点
            Node newAfterNode = new Node(newNode.data, null);
            newAfterNode.next = head.next;
            head.next = newAfterNode;
            newNode.next = head;
            return newNode;
        } else {
            Node temp = head;// 临时节点
            while (!temp.next.data.equals(target.data)) { //下个节点的值不能等于target
                temp = temp.next;
                if (temp.next == null) { // 代表循环到链表尾部，防止空指针
                    break;
                }
            }
            if (temp.next == null || !temp.next.data.equals(target.data)) {
                System.out.println(target.data + "节点未找到！");
                return null;
            }
            Node realTarget = temp.next;
            System.out.print("真正的目标节点：");
            printNode(realTarget);
            System.out.print(realTarget.data + "的前节点：");
            printNode(temp);
            // 后插 不影响前续指针
            Node newAfterNode = new Node(newNode.data, null);
            newAfterNode.next = realTarget.next;
            realTarget.next = newAfterNode;
            // 前插 会影响后续指针
            newNode.next = temp.next;
            temp.next = newNode;
        }
        return head;
    }

    public static void main(String[] args) {
        Node h = new Node("H", null);
        Node g = new Node("G", h);
        Node f = new Node("F", g);
        Node e = new Node("E", f);
        Node d = new Node("D", e);
        Node c = new Node("C", d);
        Node b = new Node("B", c);
        Node head = new Node("A", b);

        // printLinkedList(head);

        // printNode(findByValue(head, "G"));
        // printNode(findByIndex(head, 4));

        // printLinkedList(insertToHead(head, new Node("S", null)));
        // printLinkedList(insertToTail(head, new Node("Z", null)));

        printLinkedList(insertBeforeAndAfter(head, new Node("E", null), new Node("X", null)));

    }

}