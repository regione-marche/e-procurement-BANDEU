function popupValidazioneIDGARA(numgara) {
	openPopUpCustom("href=w3/commons/popup-validazione-idgaracig.jsp&entita=W3GARA&numgara=" + numgara, "validazioneIDGARA", 500, 450, "yes", "yes");
}
	
function popupRichiestaIDGARA(numgara) {
	openPopUpCustom("href=w3/commons/popup-avvia-richiesta-idgaracig.jsp&entita=W3GARA&numgara=" + numgara, "richiestaIDGARA", 600, 400, "yes", "yes");
}

function popupModificaIDGARA(numgara) {
	openPopUpCustom("href=w3/commons/popup-avvia-modifica-idgaracig.jsp&entita=W3GARA&numgara=" + numgara, "modificaGARA", 600, 400, "yes", "yes");
}

function popupCancellaIDGARA(numgara) {
	openPopUpCustom("href=w3/commons/popup-cancella-idgaracig.jsp&entita=W3GARA&numgara=" + numgara + "&numlott=1", "cancellaGARA", 600, 400, "yes", "yes");
}

function popupValidazioneCIG(numgara, numlott) {
	openPopUpCustom("href=w3/commons/popup-validazione-idgaracig.jsp&entita=W3LOTT&numgara=" + numgara + "&numlott=" + numlott, "validazioneCIG", 500, 450, "yes", "yes");
}
	
function popupRichiestaCIG(numgara, numlott) {
	openPopUpCustom("href=w3/commons/popup-avvia-richiesta-idgaracig.jsp&entita=W3LOTT&numgara=" + numgara + "&numlott=" + numlott, "richiestaCIG", 600, 400, "yes", "yes");
}

function popupModificaCIG(numgara, numlott) {
	openPopUpCustom("href=w3/commons/popup-avvia-modifica-idgaracig.jsp&entita=W3LOTT&numgara=" + numgara + "&numlott=" + numlott, "modificaLOTTO", 600, 400, "yes", "yes");
}

function popupCancellaCIG(numgara, numlott) {
	openPopUpCustom("href=w3/commons/popup-cancella-idgaracig.jsp&entita=W3LOTT&numgara=" + numgara + "&numlott=" + numlott, "cancellaLOTTO", 600, 400, "yes", "yes");
}

function popupValidazionePubblica(numgara) {
	openPopUpCustom("href=w3/commons/popup-validazione-idgaracig.jsp&entita=W3GARAPUBBLICA&numgara=" + numgara, "validazioneIDGARA", 500, 450, "yes", "yes");
}

function popupValidazioneRequisiti(numgara, numreq) {
	openPopUpCustom("href=w3/commons/popup-validazione-idgaracig.jsp&entita=W3GARAREQ&numgara=" + numgara + "&numreq=" + numreq, "validazioneRequisiti", 500, 450, "yes", "yes");
}

function popupPubblicaGaraLotto(numgara) {
	openPopUpCustom("href=w3/w3gara/w3gara-avvia-pubblica-gara-lotto.jsp&entita=W3GARAPUBBLICA&numgara=" + numgara, "pubblicaGARALOTTO", 600, 400, "yes", "yes");
}

function popupElaboraRichiesteLotti(numgara) {
	openPopUpCustom("href=w3/w3gara/w3gara-elabora-richieste-lotti.jsp&numgara=" + numgara, "elaboraRichiesteLotti", 600, 400, "yes", "yes");
}

function popupRichiestaRequisiti(numgara, numreq, idgara) {
	openPopUpCustom("href=w3/commons/popup-avvia-richiesta-requisiti.jsp&entita=W3GARAREQ&numgara=" + numgara + "&numreq=" + numreq + "&idgara=" + idgara, "richiestaRequisiti", 600, 400, "yes", "yes");
}

