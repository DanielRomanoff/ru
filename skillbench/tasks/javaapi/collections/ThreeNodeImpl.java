package ru.skillbench.tasks.javaapi.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class TreeNodeImpl implements TreeNode{
    private TreeNode parent;
    private List<TreeNode> childrenList;
    private boolean isExpanded;
    private Object data;



    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    /**
     * Возвращает корень дерева, содержащего данный объект <code>TreeNode</code>.
     * @return корневой узел. Или <code>null</code>, если у данного узла нет родителя.
     */
    @Override
    public TreeNode getRoot() {
        TreeNode root = getParent();
        if(root != null) {
            while(root.getParent() != null) {
                root = root.getParent();
            }
        }
        return root;
    }
    /**
     * Возвращает <code>false</code>, если <code>TreeNode</code> имеет ненулевое число дочерних узлов.
     * @return <code>true</code>, если данный узел является листовым (т.е. не имеет дочерних узлов)
     */
    @Override
    public boolean isLeaf() {
        return childrenList == null || childrenList.size() == 0;
    }
    /**
     * Возвращает число дочерних узлов данного <code>TreeNode</code>.
     */
    @Override
    public int getChildCount() {
        return isLeaf() ? 0 : childrenList.size();
    }

    @Override
    public Iterator<TreeNode> getChildrenIterator() {
        return childrenList.iterator();
    }

    @Override
    public void addChild(TreeNode child) {
        if(isLeaf()) {
            childrenList = new LinkedList<>();
        }
        childrenList.add(child);
        child.setParent(this);
    }

    @Override
    public boolean removeChild(TreeNode child) {
        Iterator<TreeNode> iterator = getChildrenIterator();
        TreeNode treeNode;
        while(iterator.hasNext()) {
            if((treeNode = iterator.next()).equals(child)) {
                treeNode.setParent(null);
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    /**
     * Возвращает признак "развернутости / свернутости" данного <code>TreeNode</code>
     *  (в UI-компонентах типа "дерево" от этого зависит иконка и показ дочерних узлов).<br/>
     * Узел "свернут" по умолчанию - то есть, если {@link #setExpanded(boolean)} не вызывался.
     * @return <code>true</code> - если узел развернут, <code>false</code></code></code> - если узел "свернут" (collapsed)

     */
    @Override
    public boolean isExpanded() {
        return isExpanded;
    }


    @Override
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
        if(!isLeaf()){
            Iterator<TreeNode> iterator = getChildrenIterator();
            while(iterator.hasNext()) {
                iterator.next().setExpanded(expanded);
            }
        }
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }
    /**
     * Возвращает строковое представление пути от корня дерева до данного <code>TreeNode</code>.<br/>
     * Элементы пути разделяются символами "->".<br/>
     * Каждый элемент пути - это либо getData().toString(), либо строка "empty", если getData()==null.<br/>
     * Например: "rootNode0->node1->node13->empty" ("rootNode0" - это в данном примере результат вызова метода
     *  getRoot().getData().toString() ).
     */
    @Override
    public String getTreePath() {
        StringBuilder treePath = new StringBuilder(getData() == null ? "empty" : getData().toString());
        if(getParent() != null) {
            return treePath.insert(0, getParent().getTreePath() + "->").toString();
        }
        return treePath.toString();
    }
    /**
     * Среди цепочки родительских узлов данного <code>TreeNode</code> метод находит (первый) узел с заданным объектом <code>data</code>.<br/>
     * По соглашению, "цепочка родительских узлов" содержит сам данный узел (то есть, возможно следующее: obj.findParent(*) == obj).<br/>
     * Объекты <code>data</code> должны сравниваться с помощью {@link Object#equals(Object)}, а если <code>data == null</code>,
     *  тогда должен возвращаться родительский узел, у которого <code>getData() == null</code>).
     * @param data Объект поиска; может быть равен <code>null</code>
     * @return Найденный узел. Или <code>null</code> если не было найдено узла, содержащего такой <code>data</code>.
     */
    @Override
    public TreeNode findParent(Object data) {
        if(getParent() != null) {
            return getParent().findParent(data);
        }
        else if((getData() != null && getData().equals(data)) || (getData() == null && data == null)) {
            return this;
        }
        return null;
    }

    @Override
    public TreeNode findChild(Object data) {
        if(childrenList != null) {
            for(TreeNode child : childrenList) {
                if((data == null && child.getData() == null) ||
                        (child.getData() != null && child.getData().equals(data)) ||
                        ((child = child.findChild(data)) != null)) {
                    return child;
                }
            }
        }
        return null;
    }
}