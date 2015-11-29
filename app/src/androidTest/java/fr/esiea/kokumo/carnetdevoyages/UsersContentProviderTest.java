package fr.esiea.kokumo.carnetdevoyages;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;

import fr.esiea.kokumo.carnetdevoyages.db.UsersContentProvider;
import fr.esiea.kokumo.carnetdevoyages.db.UsersDBHelper;

public class UsersContentProviderTest extends ProviderTestCase2<UsersContentProvider> {

    private ContentResolver contentResolver;

    public UsersContentProviderTest() {
        super(UsersContentProvider.class, "fr.esiea.kokumo.carnetdevoyages");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.contentResolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.cleanDatabase();
    }

    /**
     * Delete the database.
     */
    private void cleanDatabase() {
        this.contentResolver.delete(UsersContentProvider.CONTENT_URI, null, null);
    }

    /**
     * Checks if a basic request returns some results.
     */
    public void testQuery() {
        Cursor cursor = this.contentResolver.query(UsersContentProvider.CONTENT_URI, null, null, null, null);
        assertNotNull(cursor);
    }

    /**
     * Tests to insert data.
     * Checks if the data were correctly inserted into the database.
     */
    public void testInsert() {
        Uri myRowUri = this.contentResolver.insert(UsersContentProvider.CONTENT_URI,
                getValue());
        Log.i("Test", myRowUri.toString());
        assertNotNull (myRowUri);
        Cursor cursor = this.contentResolver.query(UsersContentProvider.CONTENT_URI,
                null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        Cursor cursor2 = contentResolver.query(myRowUri, null, null, null, null);
        assertNotNull(cursor2);
        assertEquals(1, cursor2.getCount());
    }

    /**
     * Returns fake values for testing purpose.
     *
     * @return
     */
    private ContentValues getValue() {
        ContentValues testValues = new ContentValues();
        testValues.put(UsersDBHelper.KEY_ID, "1");
        testValues.put(UsersDBHelper.KEY_LOGIN, "nico");
        testValues.put(UsersDBHelper.KEY_PASSWORD, "azerty");
        return testValues;
    }

}
