package cz.smartfine.pc.query;

import cz.smartfine.model.NetworkTicket;
import cz.smartfine.pc.SFPCClient;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Model tabulky pro zobrazení PL.
 *
 * @author Pavel Brož
 */
public class TicketTableModel extends AbstractTableModel {

    private List<NetworkTicket> tickets;
    private ArrayList<Column> cols = new ArrayList<Column>();

    public TicketTableModel(List<NetworkTicket> tickets) {
        super();
        this.tickets = tickets;

        cols.add(new UploaderBadgeNumberColumn(SFPCClient.getApplication().getLocalization().getString("ticket.uploaderbadgenumber.column.name")));
        cols.add(new BadgeNumberColumn(SFPCClient.getApplication().getLocalization().getString("ticket.badgenumber.column.name")));
        cols.add(new CityColumn(SFPCClient.getApplication().getLocalization().getString("ticket.city.column.name")));
        cols.add(new DateColumn(SFPCClient.getApplication().getLocalization().getString("ticket.date.column.name")));
        cols.add(new LocationColumn(SFPCClient.getApplication().getLocalization().getString("ticket.location.column.name")));
        cols.add(new MoveableDZColumn(SFPCClient.getApplication().getLocalization().getString("ticket.moveabledz.column.name")));
        cols.add(new MpzColumn(SFPCClient.getApplication().getLocalization().getString("ticket.mpz.column.name")));
        cols.add(new NumberColumn(SFPCClient.getApplication().getLocalization().getString("ticket.number.column.name")));
        cols.add(new SpzColumn(SFPCClient.getApplication().getLocalization().getString("ticket.spz.column.name")));
        cols.add(new SpzColorColumn(SFPCClient.getApplication().getLocalization().getString("ticket.spzcolor.column.name")));
        cols.add(new StreetColumn(SFPCClient.getApplication().getLocalization().getString("ticket.street.column.name")));
        cols.add(new TowColumn(SFPCClient.getApplication().getLocalization().getString("ticket.tow.column.name")));
        cols.add(new VehicleBrandColumn(SFPCClient.getApplication().getLocalization().getString("ticket.vehiclebrand.column.name")));
        cols.add(new VehicleTypeColumn(SFPCClient.getApplication().getLocalization().getString("ticket.vehicletype.column.name")));
        cols.add(new CollectionColumn(SFPCClient.getApplication().getLocalization().getString("ticket.collection.column.name")));
        cols.add(new LawNumberColumn(SFPCClient.getApplication().getLocalization().getString("ticket.lawnumber.column.name")));
        cols.add(new LetterColumn(SFPCClient.getApplication().getLocalization().getString("ticket.letter.column.name")));
        cols.add(new ParagraphColumn(SFPCClient.getApplication().getLocalization().getString("ticket.paragraph.column.name")));
        cols.add(new RuleOfLawColumn(SFPCClient.getApplication().getLocalization().getString("ticket.ruleoflaw.column.name")));
    }


    @Override
    public int getColumnCount() {
        return cols.size();
    }

    @Override
    public int getRowCount() {
        return tickets.size();
    }

    @Override
    public String getColumnName(int col) {
        return cols.get(col).getName();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return cols.get(col).getValue(row, tickets);
    }

    @Override
    public Class getColumnClass(int c) {
        return cols.get(c).getValue(0, tickets).getClass();
    }

    public List<NetworkTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<NetworkTicket> tickets) {
        this.tickets = tickets;
        super.fireTableDataChanged();
    }

    
    abstract class Column {

        String name;

        public Column(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public abstract Object getValue(int row, List<NetworkTicket> tickets);
    }

    class UploaderBadgeNumberColumn extends Column {

        public UploaderBadgeNumberColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getUploaderBadgeNumber();
        }
    }

    class BadgeNumberColumn extends Column {

        public BadgeNumberColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getBadgeNumber();
        }
    }

    class CityColumn extends Column {

        public CityColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getCity();
        }
    }

    class DateColumn extends Column {

        public DateColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getDate();
        }
    }

    class LocationColumn extends Column {

        public LocationColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getLocation();
        }
    }

    class MoveableDZColumn extends Column {

        public MoveableDZColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).isMoveableDZ();
        }
    }

    class MpzColumn extends Column {

        public MpzColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getMpz();
        }
    }

    class NumberColumn extends Column {

        public NumberColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getNumber();
        }
    }

    class SpzColumn extends Column {

        public SpzColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getSpz();
        }
    }

    class SpzColorColumn extends Column {

        public SpzColorColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getSpzColor();
        }
    }

    class StreetColumn extends Column {

        public StreetColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getStreet();
        }
    }

    class TowColumn extends Column {

        public TowColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).isTow();
        }
    }

    class VehicleBrandColumn extends Column {

        public VehicleBrandColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getVehicleBrand();
        }
    }

    class VehicleTypeColumn extends Column {

        public VehicleTypeColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getVehicleType();
        }
    }

    class CollectionColumn extends Column {

        public CollectionColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getCollection();
        }
    }

    class LawNumberColumn extends Column {

        public LawNumberColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getLawNumber();
        }
    }

    class LetterColumn extends Column {

        public LetterColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getLetter();
        }
    }

    class ParagraphColumn extends Column {

        public ParagraphColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getParagraph();
        }
    }

    class RuleOfLawColumn extends Column {

        public RuleOfLawColumn(String name) {
            super(name);
        }

        @Override
        public Object getValue(int row, List<NetworkTicket> tickets) {
            return tickets.get(row).getRuleOfLaw();
        }
    }
}