//
// Scaled C++ Mode - a Scaled major mode for editing C++ code
// http://github.com/scaled/cplusplus-mode/blob/master/LICENSE

package scaled.csharp

import scaled._
import scaled.code.{CodeConfig, Commenter}
import scaled.grammar.{Grammar, GrammarConfig, GrammarCodeMode}
import scaled.util.Paragrapher

object CppConfig extends Config.Defs {
  import CodeConfig._
  import GrammarConfig._

  // map TextMate grammar scopes to Scaled style definitions
  val effacers = List(
    effacer("comment.line", commentStyle),
    effacer("comment.block", docStyle),
    effacer("constant", constantStyle),
    effacer("invalid", invalidStyle),
    effacer("keyword", keywordStyle),
    effacer("string", stringStyle),

    effacer("entity.name.package", moduleStyle),
    effacer("entity.name.type", typeStyle),
    effacer("entity.name.tag", constantStyle),
    effacer("entity.other.inherited-class", typeStyle),
    effacer("entity.name.function", functionStyle),
    effacer("entity.name.val-declaration", variableStyle),

    effacer("meta.method.annotation", preprocessorStyle),

    effacer("storage.modifier", keywordStyle),
    effacer("storage.type", typeStyle),

    effacer("variable.package", moduleStyle),
    effacer("variable.import", typeStyle),
    effacer("variable.language", constantStyle),
    // effacer("variable.parameter", variableStyle), // leave params white
    effacer("variable.other.type", variableStyle)
  )

  // map TextMate grammar scopes to Scaled syntax definitions
  val syntaxers = List(
    syntaxer("comment.line", Syntax.LineComment),
    syntaxer("comment.block", Syntax.DocComment),
    syntaxer("constant", Syntax.OtherLiteral),
    syntaxer("string.quoted.double", Syntax.StringLiteral)
  )

  val ndfs = Seq("Platform.ndf", "Functions.ndf", "C.ndf", "Cpp.ndf")
  val grammars = resource(ndfs)(Grammar.parseNDFs)
}

@Major(name="cpp",
       tags=Array("code", "project", "cpp", "c"),
       pats=Array(".*\\.h", ".*\\.hh", ".*\\.c", ".*\\.cc", ".*\\.cpp"),
       desc="A major editing mode for the C & C++ languages.")
class CppMode (env :Env) extends GrammarCodeMode(env) {
  import CodeConfig._
  import scaled.util.Chars._

  override def configDefs = CppConfig :: super.configDefs

  override def grammars = CppConfig.grammars.get
  override def effacers = CppConfig.effacers
  override def syntaxers = CppConfig.syntaxers

  override protected def createIndenter = new CppIndenter(config)

  override val commenter = new Commenter() {
    override def linePrefix  = "//"
    override def blockOpen   = "/*"
    override def blockPrefix = "*"
    override def blockClose  = "*/"
    override def docOpen     = "///"
    override def docPrefix   = "///"
  }

  // TODO: more things!
}
