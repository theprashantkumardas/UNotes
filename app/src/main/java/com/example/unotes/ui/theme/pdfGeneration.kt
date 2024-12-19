package com.example.unotes.ui.theme

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

fun generatePdf(context: Context, noteTitle: String, noteDescription: String): File {
    // Initialize the PDF document
    val document = Document()

    // Define the directory where the PDF will be saved
    val pdfDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Notes")
    if (!pdfDir.exists()) pdfDir.mkdirs()

    // Create a PDF file with the given title
    val pdfFile = File(pdfDir, "$noteTitle.pdf")

    // Write content into the PDF file
    PdfWriter.getInstance(document, FileOutputStream(pdfFile))
    document.open()
    document.addTitle(noteTitle)
    document.add(Paragraph("Title: $noteTitle"))
    document.add(Paragraph("Description:"))
    document.add(Paragraph(noteDescription))
    document.close()

    return pdfFile
}

fun shareText(context: Context, title: String, description: String) {
    // Create the share intent
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Shared Note") // Optional: Set a subject for sharing
        putExtra(Intent.EXTRA_TEXT, "Title: $title\nDescription: $description")
    }

    // Start the chooser intent
    val chooserIntent = Intent.createChooser(shareIntent, "Share Note as Text")
    context.startActivity(chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

fun sharePdf(context: Context, pdfFile: File) {
    // Get URI for the PDF file
    val pdfUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Provider defined in the AndroidManifest
        pdfFile
    )

    // Create the intent to share the file
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, pdfUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    // Start the share intent
    context.startActivity(Intent.createChooser(shareIntent, "Share Note as PDF"))
}