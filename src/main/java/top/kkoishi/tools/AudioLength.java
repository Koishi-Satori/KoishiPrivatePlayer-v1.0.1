package top.kkoishi.tools;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.File;
import java.io.IOException;
import java.util.AbstractList;

public class AudioLength {
    public int getLength (String path) throws IOException {
        try {
            MP3File file = (MP3File) AudioFileIO.read(new File(path));
            MP3AudioHeader header = (MP3AudioHeader) file.getAudioHeader();
            return header.getTrackLength();
        } catch (CannotReadException | TagException | IOException |
                ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String[] getInfo (String path) {
        try {
            MP3File file = (MP3File) AudioFileIO.read(new File(path));
            AbstractID3v2Tag tag = file.getID3v2Tag();
            String[] info = new String[3];
            info[0] = tag.getFirst(FieldKey.ALBUM);
            info[1] = tag.getFirst(FieldKey.ALBUM_ARTIST);
            info[2] = tag.getFirst(FieldKey.TITLE);
            return info;
        } catch (CannotReadException | IOException | TagException |
                ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return null;
    }
}
