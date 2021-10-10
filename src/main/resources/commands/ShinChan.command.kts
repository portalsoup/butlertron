import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command

command {
    name = "Shin-chan quotes"
    description = "A collection of shin-chan quotes"

    help {
        description = ""
        trigger = ""

        action(
            "",
            "",
            ""
        )
    }

    job {
        precondition {
            it.formattedMessage().matches(Regex("do\\sone\\sof\\syour\\slittle\\sdances.*"))
        }

        action {
            it.reply("A traditional Ass-Dance will cost you *TWO* boxes of Chocobees.")
        }
    }

    job {
        precondition {
            it.formattedMessage().contains("i've got a meal on the motorway to the floater-way. you say?")
        }
        action {
            it.reply("Don't rush the flush, Daddio! Listen to the ass! Pass the gas! Listen to the ass! Pass the gas!")
        }
    }

    job {
        precondition {
            it.formattedMessage().equals("what'll i get for one box?")
        }
        action {
            it.reply("One *CHEEK!*")
        }
    }
}