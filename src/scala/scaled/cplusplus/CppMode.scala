//
// Scaled C++ Mode - a Scaled major mode for editing C++ code
// http://github.com/scaled/cplusplus-mode/blob/master/LICENSE

package scaled.cplusplus

import scaled._
import scaled.code.{Commenter, BlockIndenter}
import scaled.grammar.GrammarCodeMode

@Major(name="cpp",
       tags=Array("code", "project", "cpp", "c"),
       pats=Array(".*\\.h", ".*\\.hh", ".*\\.c", ".*\\.cc", ".*\\.cpp"),
       desc="A major editing mode for the C & C++ languages.")
class CppMode (env :Env) extends GrammarCodeMode(env) {

  override def langScope = "source.c++"

  override protected def createIndenter = new BlockIndenter(config, Std.seq(
    // bump extends/implements in two indentation levels
    BlockIndenter.adjustIndentWhenMatchStart(Matcher.regexp("(extends|implements)\\b"), 2),
    // align changed method calls under their dot
    new BlockIndenter.AlignUnderDotRule(),
    // handle javadoc and block comments
    new BlockIndenter.BlockCommentRule(),
    // handle indenting switch statements properly
    new BlockIndenter.SwitchRule(),
    // handle continued statements, with some special sauce for : after case
    new BlockIndenter.CLikeContStmtRule()
  ))

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
