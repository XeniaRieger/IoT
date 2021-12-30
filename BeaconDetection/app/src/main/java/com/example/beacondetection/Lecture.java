package com.example.beacondetection;

public class Lecture implements Comparable<Lecture> {

    private String course, lecture, date, time, room;

    public void setCourse(String course) {
        this.course = course;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCourse() {
        return course;
    }

    public String getLecture() {
        return lecture;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public int compareTo(Lecture o) {
        return (this.getDate()).compareTo(o.getDate());
    }
}
