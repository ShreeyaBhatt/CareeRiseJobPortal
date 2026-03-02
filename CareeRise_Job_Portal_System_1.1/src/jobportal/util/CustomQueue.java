package jobportal.util;

import jobportal.jobs.Job;

public class CustomQueue {

    private static class Node {
        Job job;
        Node next;

        Node(Job job) {
            this.job = job;
            this.next = null;
        }
    }

    private Node front;
    private Node rear;
    private int capacity;

    // Constructor initializes empty queue
    public CustomQueue(int capacity) {
        this.front = null;
        this.rear = null;
        this.capacity = 0;
    }

    // Add job to end of queue
    public void enqueue(Job item) {
        Node newNode = new Node(item);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        capacity++;
    }

    // Remove and return job from front of queue
    public Job dequeue() {
        if (isEmpty()) {
            return null;
        }
        Job job = front.job;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        capacity--;
        return job;
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return capacity == 0;
    }

    // Return the current number of elements
    public int getCapacity() {
        return capacity;
    }
}