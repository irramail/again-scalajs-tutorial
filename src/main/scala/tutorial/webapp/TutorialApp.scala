package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext._
import org.scalajs.dom.html
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.JSON
import scala.scalajs.js


class Example[Builder, Output <: FragT, FragT]
             (val bundle: scalatags.generic.Bundle[Builder, Output, FragT]){
  val htmlFrag = {
    import bundle.all._

    def props(): Array[String] = {
      var tmpLoadSettings = dom.window.localStorage.getItem("settings")
      val  ares = "1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080"

      if (tmpLoadSettings == null) { 
        tmpLoadSettings="||||||"+ares
      }

      tmpLoadSettings += "|end"

      tmpLoadSettings.split('|')
    }

    def inputText(idname: String, sz: String, ph: String, content: String) = input(
      id := idname,
      name := idname,
      size := sz,
      placeholder := ph,
      value := content
    )

    def inputButton(v: String, fn: String) = input(
      attr("type") := "button",
      value := v,
      onclick := fn
    )

    val help = Seq(
    "1. Заполнить все однострочные поля.",
    "2. Отметить форматы изображения.",
    "3. Нажать кнопку Set, чтобы сохранить и отправить настройки на сервер.",
    "4. Справа вверху нажать на кнопку Upload Images и выбрать изображение формата JPG.",
    "5. Подождать пока в многострочном текстовом поле появится сгенерированный html текст.",
    "6. Чтобы скачать архив с изображениями, следует нажать по ссылке Download (расположена внизу рабочей области).",
    "7. Поменять любое из полей или выбрать другой набор разрешений, нажать Retry, процесс генерации будет запущен повторно с последним загруженным изображением."
    )

    val settings = props()

    val r = scala.util.Random
    val u="_"
    val  ares = "1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080"

    div(
      div(
        for (itemHelp<- help) yield div(
          itemHelp
        )
      ),
    
      for (aaa  <- ares.split(";").toSeq) yield
        for( res <- aaa.split('_') if res.contains('x')) yield
          for(r <- (res+",br").split(',').toSeq) yield 
            if (r.length > 2 )
              span(
                input(
                  attr("type"):="checkbox",
                  attr("id"):=aaa.split('_')(0) + '_' + r,
                  attr("value"):=aaa.split('_')(0) + '_' + r,
                  attr("class"):="res",
                  attr("style"):="transform: scale(1.4);padding-top:10px;"
                ),
                label(
                  attr("style"):="margin-right: 10px;",
                  attr("for"):=aaa.split('_')(0) + '_' + r,
                  aaa.split('_')(0) + ' ' + r
                )
              )
            else br,

      inputText("url", "50", "https://", settings(0)),
      inputText("fname", "30", "Image", settings(1)),
      inputButton("Set", "set()"),
      inputButton("Retry", "reTry()"),
      inputButton("Send to server", "upload()"),
      br,
      inputText("alt", "132", "alt", settings(3)), br,
      inputText("metaName", "132", "Meta name", settings(4)), br,
      inputText("metaDescription", "132", "Meta description", settings(5)), br,
      inputText("description", "132", "Thumbnail description", settings(2)), br,
      textarea(id := "textLines", name := "text", cols := "130", rows := "15", wrap := "off"), br,
      
      div(
        id := "wrapperTrack",
        div(
          id := "track",
          style := "display: block;",

          img(
            src := "/schema/schemaImg_1.jpg?nocache="+r.nextInt(1000000000),
            alt := "Image",
            attr("height") := "100px",
            attr("decoding") := "async"
          ),

          a(
            href := "/schema/schema.tgz",
            style := "position: relative; top: -90px",
            attr("download").empty,
            "Download"
          )
        )
      )
    )
  }
}

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
  var tmp = ""
  var map5: Map[String, String] = Map();
  var ares = ""
  //document.querySelectorAll("input[type='checkbox']:checked").map(x => ares += x.asInstanceOf[html.Input].value + ";")

  document.querySelectorAll("input[type='checkbox']:checked").map(x => {
    val a_res = x.asInstanceOf[html.Input].value.split("_")
    val a = a_res(0)
    if (!map5.contains(a))
      map5 += (a -> "")
    map5 += (a -> (map5(a) + a_res(1) + ","))
  })

  map5.keys.foreach(i => ares += (i + "_" + map5(i).stripSuffix(",").trim + ";"))

  ares = ares.stripSuffix(";").trim

  val settings = document.getElementById("url").asInstanceOf[html.Input].value + '|' +
  document.getElementById("fname").asInstanceOf[html.Input].value + '|' +
    document.getElementById("description").asInstanceOf[html.Input].value + '|' +
    document.getElementById("alt").asInstanceOf[html.Input].value + '|' +
    document.getElementById("metaName").asInstanceOf[html.Input].value + '|' +
    document.getElementById("metaDescription").asInstanceOf[html.Input].value + '|' +
    ares;
    dom.window.localStorage.setItem(
      "settings", settings
    )

  request(settings, "set")
}

@JSExportTopLevel("reTry")
def reTry(): Unit = {
  set()

  request("", "reTry")
}

@JSExportTopLevel("upload")
def upload(): Unit = {
  request("", "upload")
}

def requestOnloadSuccess() {
    //println("insuccess")
    class Settings extends js.Object {
      var jsonrpc: String = "2.0"
      var method: String = _
      var id: Int = 1
      var params: js.Array[String] = _
    }
    val sets = new Settings()
    sets.method = "get_schema_org"
    sets.params = js.Array("")

  val xhr = new dom.XMLHttpRequest()
  xhr.open("POST",
    "/api"
  )
  xhr.onload = { (e: dom.Event) =>
    if (xhr.status == 200) {
      val allText = js.JSON.parse(xhr.responseText).result.asInstanceOf[String]
      //println(allText)
      document.getElementById("textLines").asInstanceOf[html.Element].innerHTML = allText
      dom.window.localStorage.setItem(
        "textLines", allText
      )

      val insideDiv = document.getElementById("wrapperTrack").asInstanceOf[html.Div]

      val r = scala.util.Random
      insideDiv.innerHTML = "<div id=\"track\" style=\"display: block;\"><img src=\"/schema/schemaImg_1.jpg?nocache='" + r.nextInt(1000000000) + "'\" alt=\"img\" height=\"100px\"><a href=\"/schema/schema.tgz\" download style=\"position: relative; top: -90px;\">Download</a></div>"
    }
  }
  xhr.setRequestHeader("Content-Type", "application/json")

  xhr.send(JSON.stringify(sets))
}

def requestOnload() {
    //println("brgin onload")
    class Settings extends js.Object {
      var jsonrpc: String = "2.0"
      var method: String = _
      var id: Int = 1
      var params: js.Array[String] = _
    }
    val sets = new Settings()
    sets.method = "exists_img"
    sets.params = js.Array("")

  val xhr = new dom.XMLHttpRequest()
  xhr.open("POST",
    "/api"
  )
  xhr.onload = { (e: dom.Event) =>
    if (xhr.status == 200) {
      //println(js.JSON.parse(xhr.responseText).result)
      if (js.JSON.parse(xhr.responseText).result.asInstanceOf[String] == "true") {
        if (document.getElementById("track").asInstanceOf[html.Div].style.display == "block") {
          //println("block to none")
          document.getElementById("track").asInstanceOf[html.Div].style.display = "none"
        }
      } else {
        //println("else")
        if (document.getElementById("track").asInstanceOf[html.Div].style.display == "none") {
          //println("none to block")
          requestOnloadSuccess()
        }

      }
    }
  }
  xhr.setRequestHeader("Content-Type", "application/json")

  xhr.send(JSON.stringify(sets))
}

def request(settings: String, method: String) {

    class Settings extends js.Object {
      var jsonrpc: String = "2.0"
      var method: String = _
      var id: Int = 1
      var params: js.Array[String] = _
    }
    val sets = new Settings()
    sets.method = method
    sets.params = js.Array(settings)

  val xhr = new dom.XMLHttpRequest()
  xhr.open("POST",
    "/api"
  )
  xhr.onload = { (e: dom.Event) =>
    if (xhr.status == 200) {
      //println(xhr.responseText)
    }
  }
  xhr.setRequestHeader("Content-Type", "application/json")

  xhr.send(JSON.stringify(sets))
}

def main(args: Array[String]): Unit = {

  val textExample = new Example(scalatags.Text)
  dom.document.getElementById("content").innerHTML = textExample.htmlFrag.render

  document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>

    val u="_"
    val  ares = "1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080"

    var tmpLoadSettings = dom.window.localStorage.getItem("settings")

    if (tmpLoadSettings == null) { 
      tmpLoadSettings="||||||"+ares
    }

    tmpLoadSettings += "|end"

    val settingsStorage = tmpLoadSettings.split('|')

    var checked = settingsStorage(6)

    if (checked.getClass().toString == "class java.lang.Void")
      checked = ares

    if (checked.length > 0) {
      checked.split(";").foreach( ar => {
        val item_ar = ar.split('_')
        val aa = item_ar(0)
        item_ar(1).split(',').foreach( res => {
          document.getElementById(s"$aa$u$res").setAttribute("checked", "")
        }) 
      })
    }

    dom.window.setInterval(() =>
      requestOnload()
    , 1000) 

  })
}
}