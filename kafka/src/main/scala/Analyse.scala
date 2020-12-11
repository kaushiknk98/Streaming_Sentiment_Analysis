import java.util.Properties

import Sentiment.Sentiment

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.collection.convert.wrapAll._

object Analyse {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")

  val pipe = new StanfordCoreNLP(props)

  def get(input: String): Sentiment = Option(input) match {
    case Some(text) if !text.isEmpty =>
      val (_, sentiment) = extract(text).maxBy { case (sentence, _) => sentence.length }
      return sentiment
    case _ => throw new IllegalArgumentException("Error: Input received is null or empty.")
  }

  def extract(text: String): List[(String, Sentiment)] = {
    val lines = pipe.process(text).get(classOf[CoreAnnotations.SentencesAnnotation])
    return lines.map(line => (line, line.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree]))).map { case (line, tree) => (line.toString, Sentiment.toSentiment(RNNCoreAnnotations.getPredictedClass(tree))) }.toList
  }
}