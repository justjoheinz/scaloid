$license()$

package org.scaloid.common

import android.content._
import android.graphics.Movie
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view._
import language.implicitConversions

private[scaloid] class UnitConversion(val ext: Double)(implicit context: Context) {
  @inline private def m = context.getResources.getDisplayMetrics
  @inline def dip   : Int = (ext * m.density).toInt
  @inline def sp    : Int = (ext * m.scaledDensity).toInt
  @inline def px2dip: Int = (ext / m.density).toInt
  @inline def px2sp : Int = (ext / m.scaledDensity).toInt
}

private[scaloid] class ResourceConversion(val id: Int)(implicit context: Context) {
  @inline def r2Text         : CharSequence        = context.getText(id)
  @inline def r2TextArray    : Array[CharSequence] = context.getResources.getTextArray(id)
  @inline def r2String       : String              = context.getResources.getString(id)
  @inline def r2StringArray  : Array[String]       = context.getResources.getStringArray(id)
  @inline def r2Drawable     : Drawable            = context.getResources.getDrawable(id)
  @inline def r2Movie        : Movie               = context.getResources.getMovie(id)
}

trait ConversionImplicits {
  @inline implicit def Double2unitConversion(ext: Double)(implicit context: Context): UnitConversion = new UnitConversion(ext)(context)
  @inline implicit def Long2unitConversion  (ext: Long)  (implicit context: Context): UnitConversion = new UnitConversion(ext)(context)
  @inline implicit def Int2unitConversion   (ext: Int)   (implicit context: Context): UnitConversion = new UnitConversion(ext)(context)

  @inline implicit def Int2resource(ext: Int)(implicit context: Context): ResourceConversion = new ResourceConversion(ext)(context)

  // r2String is not provided because it is ambiguous with r2Text
  @inline implicit def r2Text       (id: Int)(implicit context: Context): CharSequence        = context.getText(id)
  @inline implicit def r2TextArray  (id: Int)(implicit context: Context): Array[CharSequence] = context.getResources.getTextArray(id)
  @inline implicit def r2StringArray(id: Int)(implicit context: Context): Array[String]       = context.getResources.getStringArray(id)
  @inline implicit def r2Drawable   (id: Int)(implicit context: Context): Drawable            = context.getResources.getDrawable(id)
  @inline implicit def r2Movie      (id: Int)(implicit context: Context): Movie               = context.getResources.getMovie(id)

  @inline implicit def string2Uri           (str: String): Uri            = Uri.parse(str)
  @inline implicit def string2IntentFilter  (str: String): IntentFilter   = new IntentFilter(str)
}
object ConversionImplicits extends ConversionImplicits

trait InterfaceImplicits {
  implicit def func2ViewOnClickListener[F](f: (View) => F): View.OnClickListener =
    new View.OnClickListener() {
      def onClick(view: View) {
        f(view)
      }
    }

  implicit def lazy2ViewOnClickListener[F](f: => F): View.OnClickListener =
    new View.OnClickListener() {
      def onClick(view: View) {
        f
      }
    }

  implicit def func2DialogOnClickListener[F](f: (DialogInterface, Int) => F): DialogInterface.OnClickListener =
    new DialogInterface.OnClickListener {
      def onClick(dialog: DialogInterface, which: Int) {
        f(dialog, which)
      }
    }

  implicit def lazy2DialogOnClickListener[F](f: => F): DialogInterface.OnClickListener =
    new DialogInterface.OnClickListener {
      def onClick(dialog: DialogInterface, which: Int) {
        f
      }
    }

  implicit def func2runnable[F](f: () => F): Runnable =
    new Runnable() {
      def run() {
        f()
      }
    }

  implicit def lazy2runnable[F](f: => F): Runnable =
    new Runnable() {
      def run() {
        f
      }
    }
}
object InterfaceImpliciits extends InterfaceImplicits


trait Implicits extends ConversionImplicits with InterfaceImplicits with ViewImplicits with WidgetImplicits
object Implicits extends Implicits
