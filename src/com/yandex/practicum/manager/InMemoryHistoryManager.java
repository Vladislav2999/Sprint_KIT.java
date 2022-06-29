
package com.yandex.practicum.manager;

import com.yandex.practicum.pattern.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    private final HashMap<Integer, Node> historyToEdit = new HashMap<>();
    private Node head;
    private Node tail;

    private class Node {
        private final Task data;
        private Node next;
        private Node prev;

        public Node(Task data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
        @Override
        public void add(Task task) {
            int taskId = task.getId();
            boolean isContainsNode = historyToEdit.containsKey(taskId);
            if (isContainsNode) {
                remove(taskId);
            }
            linkLast(task);
            historyToEdit.put(taskId, tail);
        }


        @Override
        public List<Task> getHistory() {
            return getTasks();
        }

        @Override
        public void remove(int id) {
            Node node = historyToEdit.get(id);
            if (node != null) {
                removeNode(node);
                historyToEdit.remove(id);

            }
        }

        public void linkLast(Task element) {
            final Node oldTail = tail;
            final Node newNode = new Node(element, null, oldTail);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            historyToEdit.put(element.getId(), newNode);
        }


        public List<Task> getTasks() {
            final ArrayList<Task> tasks = new ArrayList<>();
            Node node = head;
            int i = 0;
            while (node != null) {
                tasks.add(i++,node.data);
                node = node.prev;
            }
            return tasks;
        }

        public void removeNode(Node node) {
            final Node next = node.next;
            final Node prev = node.prev;

            if (prev == null && next != null) {
                head = next;

            } else if (next == null && prev != null) {
                tail = prev;

            } else if (next == null) {
                head = null;
                tail = null;

            } else {
                node.next = null;
                node.prev = prev;
            }
        }
    }

