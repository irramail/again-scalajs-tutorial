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

    val help = Seq(
    "1. Заполнить все однострочные поля.",
    "2. Отметить форматы изображения.",
    "3. Нажать кнопку Set, чтобы сохранить и отправить настройки на сервер.",
    "4. Справа вверху нажать на кнопку Upload Images и выбрать изображение формата JPG.",
    "5. Подождать пока в многострочном текстовом поле появится сгенерированный html текст.",
    "6. Чтобы скачать архив с изображениями, следует нажать по ссылке Download (расположена внизу рабочей области).",
    "7. Поменять любое из полей или выбрать другой набор разрешений, нажать Retry, процесс генерации будет запущен повторно с последним загруженным изображением."
    )

    val u="_"
    val  ares = "1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080"

    div(
      div(
        for (itemHelp<- help) yield div(
          itemHelp
        )
      ),


/*

*/


      /*
      h1(id:="title", "This is a title"),
      h1("Header 1"),
      h2("Header 2"),
      h3("Header 3"),
      h4("Header 4"),
      h5("Header 5"),
      h6("Header 6"),
      hr,
      p(
        "This is a paragraph with a whole bunch of ",
        "text inside. You can have lots and lots of ",
        "text"
      ),
      b("bold"), " ",
      i("italic"), " ",
      s("strikethrough"), " ",
      u("underlined"), " ",
      em("emphasis"), " ",
      strong("strong"), " ",
      sub("sub"), " ",
      sup("sup"), " ",
      code("code"), " ",
      a("a-link"), " ",
      small("small"), " ",
      cite("cite"), " ",
      ins("ins"), " ",
      del("del"), " ",
      hr,
      ol(
        li("Ordered List Item 1"),
        li("Ordered List Item 2"),
        li("Ordered List Item 3")
      ),
      ul(
        li("Unordered List Item 1"),
        li("Unordered List Item 2"),
        li("Unordered List Item 3")
      ),
      dl(
        dt("Definition List Term 1"),
        dd("Definition List Item 1"),
        dt("Definition List Term 2"),
        dd("Definition List Item 2")
      ),
      hr,
      pre(
        """
          |Preserved formatting area with all the whitespace
          |kept around and probably some kind of monospace font
        """.stripMargin
      ),
      blockquote(
        """
          |Block quote with a bunch of text inside
          |which really rox
        """.stripMargin
      ),
      hr,
      table(
        caption("This is a table caption"),
        thead(
          tr(
            th("Header Content 1"),
            th("Header Content 2")
          )
        ),
        tbody(
          tr(
            td("Body Content 1"),
            td("Body Content 2")
          ),
          tr(
            td("Body Content A"),
            td("Body Content B")
          )
        ),
        tfoot(
          tr(
            td("Foot Content 1"),
            td("Foot Content 2")
          )
        )
      ),
      hr,
      label("input"), input,
      br,
      label("select"),
      select(
        option("lol"),
        option("wtf")
      ),
      br,
      label("textarea"),
      */
      textarea
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
  dom.document.getElementById("ttt").innerHTML = textExample.htmlFrag.render

  document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
    setupUI()
    appendHelp()

    val u="_"
    val  ares = "1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080"
    ares.split(";").foreach( ar => {
      val item_ar = ar.split('_')
      val aa = item_ar(0)
      item_ar(1).split(',').foreach( res => {
        appendCheckBox(s"$aa$u$res")
      })

      appendBr()
    })

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
    appendInput("url", "50", "https://", settingsStorage(0))
    appendInput("fname", "30", "Image", settingsStorage(1))
    appendButton("Set", "set()")
    appendButton("Retry", "reTry()")
    appendButton("Send to server", "upload()")
    appendBr()
    appendInput("alt", "132", "alt", settingsStorage(3))
    appendBr()
    appendInput("metaName", "132", "Meta name", settingsStorage(4))
    appendBr()
    appendInput("metaDescription", "132", "Meta description", settingsStorage(5))
    appendBr()
    appendInput("description", "132", "Thumbnail description", settingsStorage(2))
    appendBr()
    appendTextarea()
    appendBr()
    appendDiv()

    dom.window.setInterval(() =>
      requestOnload()
    , 1000) 

//{"jsonrpc":"2.0","method":"set","id":1,"params":["https://test.domain/assets/upload|test_div_all|test_div_all thumb desrciption|test_div_all alt|test_div_all meta name|test_div_all meta description|1:1_320x320,640x640,1280x1280,1920x1920;4:3_320x240,640x480,1280x960,1920x1440;16:9_320x180,640x360,854x480,1280x720,1920x1080"]}

      //println(document.getElementById("alt").asInstanceOf[html.Input].value)
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

def appendHelp(): Unit = {
  val wrapperDiv = document.createElement("div")
  val in1Div = document.createElement("div")
  val in2Div = document.createElement("div")
  val in3Div = document.createElement("div")
  val in4Div = document.createElement("div")
  val in5Div = document.createElement("div")
  val in6Div = document.createElement("div")
  val in7Div = document.createElement("div")

  in1Div.innerHTML = "1. Заполнить все однострочные поля."
  in2Div.innerHTML = "2. Отметить форматы изображения."
  in3Div.innerHTML = "3. Нажать кнопку Set, чтобы сохранить и отправить настройки на сервер."
  in4Div.innerHTML = "4. Справа вверху нажать на кнопку Upload Images и выбрать изображение формата JPG."
  in5Div.innerHTML = "5. Подождать пока в многострочном текстовом поле появится сгенерированный html текст."
  in6Div.innerHTML = "6. Чтобы скачать архив с изображениями, следует нажать по ссылке Download (расположена внизу рабочей области)."
  in7Div.innerHTML = "7. Поменять любое из полей или выбрать другой набор разрешений, нажать Retry, процесс генерации будет запущен повторно с последним загруженным изображением."

  wrapperDiv.appendChild(in1Div)
  wrapperDiv.appendChild(in2Div)
  wrapperDiv.appendChild(in3Div)
  wrapperDiv.appendChild(in4Div)
  wrapperDiv.appendChild(in5Div)
  wrapperDiv.appendChild(in6Div)
  wrapperDiv.appendChild(in7Div)

  document.body.appendChild(wrapperDiv)
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

def appendInput(id: String, size: String, placeholder: String, content: String = ""): Unit = {
  val input = document.createElement("input")
  input.setAttribute("type", "text")
  input.setAttribute("id", id)
  input.setAttribute("name", id)
  input.setAttribute("size", size)
  input.setAttribute("placeholder", placeholder)
  input.asInstanceOf[html.Input].value = content;
  document.body.appendChild(input)
}

def appendCheckBox(id: String): Unit = {
  val checkBox = document.createElement("input")
  checkBox.setAttribute("type", "checkbox")
  checkBox.setAttribute("id", id)
  checkBox.setAttribute("name", "resolution_"+id)
  checkBox.setAttribute("value", id)
  //checkBox.setAttribute("checked", "")
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
  textarea.setAttribute("rows",  "15")
  textarea.setAttribute("wrap",  "off")
  textarea.innerHTML = dom.window.localStorage.getItem("textLines")

  document.body.appendChild(textarea)
}

def appendDiv(): Unit = {
    val wrapperDiv = document.createElement("div")
    wrapperDiv.setAttribute("id", "wrapperTrack")

    val insideDiv = document.createElement("div")
    insideDiv.setAttribute("id", "track")
    insideDiv.setAttribute("style", "display: block;")

    val img = document.createElement("img")
    val r = scala.util.Random
    img.setAttribute("src", "/schema/schemaImg_1.jpg?nocache="+r.nextInt(1000000000))
    img.setAttribute("alt", "img")
    img.setAttribute("height", "100px")
    img.setAttribute("decoding", "async")

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

