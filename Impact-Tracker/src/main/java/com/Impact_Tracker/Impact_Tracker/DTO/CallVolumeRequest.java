package com.Impact_Tracker.Impact_Tracker.DTO;

import java.util.List;


public class CallVolumeRequest {
    
    private List<Integer> answered;
    private List<Integer> missed;
    private List<Integer> voicemail;
    private List<String> months;


    
    // getters & setters



    public List<Integer> getAnswered() {
        return answered;
    }

    public void setAnswered(List<Integer> answered) {
        this.answered = answered;
    }

    public List<Integer> getMissed() {
        return missed;
    }

    public void setMissed(List<Integer> missed) {
        this.missed = missed;
    }

    public List<Integer> getVoicemail() {
        return voicemail;
    }

    public void setVoicemail(List<Integer> voicemail) {
        this.voicemail = voicemail;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    @Override
    public String toString() {
        return "CallVolumeRequest{" +
                "answered=" + answered +
                ", missed=" + missed +
                ", voicemail=" + voicemail +
                ", months=" + months +
                '}';
    }
}
