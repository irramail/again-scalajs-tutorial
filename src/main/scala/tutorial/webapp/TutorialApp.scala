package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html
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

@JSExportTopLevel("set")
def set(): Unit = {
  println(document.getElementById("alt"))
  val settings = document.getElementById("url").asInstanceOf[html.Input].value + '|' +
  document.getElementById("fname").asInstanceOf[html.Input].value + '|' +
    document.getElementById("description").asInstanceOf[html.Input].value + '|' +
    document.getElementById("alt").asInstanceOf[html.Input].value + '|' +
    document.getElementById("metaName").asInstanceOf[html.Input].value + '|' +
    document.getElementById("metaDescription").asInstanceOf[html.Input].value + '|' +
    document.getElementById("alt").asInstanceOf[html.Input].value;
  dom.window.localStorage.setItem(
      "settings", settings
    )
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
    appendButton("Set", "set()")
    appendButton("Retry", "reTry()")
    appendButton("Upload", "upload()")
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
    appendBr()
    appendDiv()
      println(document.getElementById("alt").asInstanceOf[html.Input].value)
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

def appendButton(value: String, functionName: String) {
  val input = document.createElement("input")
  input.setAttribute("type", "button")
  input.setAttribute("value", value)
  input.setAttribute("onclick", functionName)

  document.body.appendChild(input)
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

def appendDiv(): Unit = {
    val wrapperDiv = document.createElement("div")
    wrapperDiv.setAttribute("id", "wrapperTrack")

    val insideDiv = document.createElement("div")
    insideDiv.setAttribute("id", "track")

    val img = document.createElement("img")
    img.setAttribute("src", "/schema/schemaImg_1.jpg")
    img.setAttribute("alt", "img")
    img.setAttribute("height", "100px")

    val link = document.createElement("a")
    link.setAttribute("href", "/schema/schema.tgz")
    link.setAttribute("style", "position: relative; top: -90px;")
    link.setAttribute("download", "")
    link.textContent = "Download"


    insideDiv.appendChild(img)
    insideDiv.appendChild(link)


    wrapperDiv.appendChild(insideDiv)

    document.body.appendChild(wrapperDiv)
  }

}
