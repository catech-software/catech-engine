package net.catech_software.engine.model;

import java.util.ArrayList;

import org.lwjgl.assimp.*;

public class Node {
  private AINode node;
  private ArrayList<Node> children;

  public Node(AINode node) {
    this.node = node;

    this.children = new ArrayList<>();
    for (int i = 0; i < this.node.mNumChildren(); i++) this.children.add(new Node(AINode.create(this.node.mChildren().get(i))));
  }

  public void free() {
    for (int i = 0; i < this.children.size(); i++) this.children.get(i).free();
    this.children = null;
    this.node = null;
  }
}
