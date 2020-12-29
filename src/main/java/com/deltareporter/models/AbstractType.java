package com.deltareporter.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class AbstractType {
  private long id;

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return this.id;
  }

  public String serialize(){
    ObjectMapper Obj = new ObjectMapper();
    String body = "";
    try {
      body = Obj.writeValueAsString(this);
      System.out.println("Sending body: "+body);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return body;
  }
}
