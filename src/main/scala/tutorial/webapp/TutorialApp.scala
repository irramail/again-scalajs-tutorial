package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

object TutorialApp {
  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }
  
@JSExportTopLevel("addClickedMessage")
def addClickedMessage(): Unit = {
  appendPar(document.body, "You clicked the button!")
}

def main(args: Array[String]): Unit = {
  document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
    setupUI()

    val u="_"

    val ares = "1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080";
    ares.split(";").foreach( ar => {
      val item_ar = ar.split('_')
      val aa = item_ar(0)
      item_ar(1).split(',').foreach( res => {
        appendCheckBox(s"$aa$u$res")
      })
    appendBr()
    })

    appendInput("url", "50", "https://")
    appendInput("fname", "30", "Image")
    appendBr()
    appendInput("alt", "132", "alt")
    appendBr()
    appendInput("metaName", "132", "Meta name")
    appendBr()
    appendInput("metaDescription", "132", "Meta description")
    appendBr()
    appendInput("description", "132", "Thumbnail description")
    appendBr()
    appendTextarea()
  })
}

def setupUI(): Unit = {
  //val button = document.createElement("button")
  //button.textContent = "Click me!"
  //button.addEventListener("click", { (e: dom.MouseEvent) =>
    //addClickedMessage()
  //})
  //document.body.appendChild(button)
}

def appendBr(): Unit = {
  val br = document.createElement("br")
  document.body.appendChild(br)
}

def appendInput(id: String, size: String, placeholder: String): Unit = {
  val input = document.createElement("input")
  input.setAttribute("type", "text")
  input.setAttribute("id", id)
  input.setAttribute("name", id)
  input.setAttribute("size", size)
  input.setAttribute("placeholder", placeholder)
  document.body.appendChild(input)
}

def appendCheckBox(id: String): Unit = {
  val checkBox = document.createElement("input")
  checkBox.setAttribute("type", "checkbox")
  checkBox.setAttribute("id", id)
  checkBox.setAttribute("name", "resolution_"+id)
  checkBox.setAttribute("value", id)
  checkBox.setAttribute("checked", "")
  checkBox.setAttribute("class", "res")
  checkBox.setAttribute("style", "transform: scale(1.4); padding-top:10px;")

  //document.body.appendChild(checkBox)

  val label = document.createElement("label")
  label.setAttribute("for", id)
  label.setAttribute("style", "margin-right: 10px;")
  label.innerHTML=id.replace('_', ' ')

  val span = document.createElement("span")
  span.appendChild(checkBox)
  span.appendChild(label)

  document.body.appendChild(span)
}

def appendLabel(id: String): Unit = {
  val label = document.createElement("label")
  label.setAttribute("for", id)
  label.setAttribute("style", "margin-right: 10px;")
  label.innerHTML=id

  document.body.appendChild(label)
}

def appendTextarea(): Unit = {
  val textarea = document.createElement("textarea")
  textarea.setAttribute("id",  "textLines")
  textarea.setAttribute("name",  "text")
  textarea.setAttribute("cols",  "130")
  textarea.setAttribute("rows",  "18")
  textarea.setAttribute("wrap",  "off")

  document.body.appendChild(textarea)
}
}
