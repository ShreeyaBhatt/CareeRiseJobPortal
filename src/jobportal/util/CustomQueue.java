package jobportal.util;

import jobportal.jobs.Job;

public class CustomQueue {

    private static class Node {
        Job job;
        Node next;

        Node(Job job) {
            this.job = job;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public CustomQueue(int capacity) {
        front = null;
        rear = null;
        size = 0;
    }

    public void enqueue(Job item) {
        Node newNode = new Node(item);

        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public Job dequeue() {
        if (isEmpty()) return null;

        Job job = front.job;
        front = front.next;

        if (front == null) rear = null;

        size--;
        return job;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getCapacity() {
        return size;
    }
}