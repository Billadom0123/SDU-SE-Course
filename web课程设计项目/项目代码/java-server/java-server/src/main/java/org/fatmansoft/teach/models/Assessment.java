package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "assessment",
        uniqueConstraints = {
        })
public class Assessment {
    @Id
    private Integer id;



    @NotBlank
    @Size(max=500)
    private String describe;


    @ManyToOne
    @JoinColumn(name = "receiveId")
    private Student receive;


    @ManyToOne
    @JoinColumn(name = "deliverId")
    private Student deliver;

    //这条互评是否已经被查看过
    private String checked;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }



    public Student getReceive() {
        return receive;
    }

    public void setReceive(Student receive) {
        this.receive = receive;
    }



    public Student getDeliver() {
        return deliver;
    }

    public void setDeliver(Student deliver) {
        this.deliver=deliver;
    }


}

