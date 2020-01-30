import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp();

// Welcome new candidates
export const welcomeCandidate = functions.firestore.document('candidates/{candidateId}').onCreate((snapshot,context) => {
    return console.log(`Welcome message sent to candidate with ID: ${context.params.candidateId}`);
});
